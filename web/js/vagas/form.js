jQuery(function () {
    $(".areaAtuacao").chosen();
    $('#modalCadastroVaga').modal({ backdrop: 'static', keyboard: false });

    $("#btnSalvar").on('click', function () {

        console.log('Valor do btn SALVAR OU EDIT', $("#btnSalvar").val());

        if ($("#btnSalvar").val() == "Cadastrar") {
            cadastrarVaga();
        }

        if ($("#btnSalvar").val() == "Editar") {
            atualizarVaga();
        }
    });

    /*  $("#area_atuacao").on('click', function () {
         $("#area_atuacao").chosen("destroy");
     }); */

    $(".novaVaga").on('click', function () {
        $("#btnSalvar").html("Cadastrar");
        $("#btnSalvar").val("Cadastrar");

        $(".salario").show();
        limparCampos();
        carregarTags();
    });

    $("#valorCombinar").on('change', function () {
        if ($(this).is(':checked')) {
            $("#salario").val('');
            $(".salario").hide();
            return;
        }

        $(".salario").show();
    });
});

function carregarTags(tags = null) {
    let token = localStorage.getItem('token');

    console.log('Valor do tags', tags);

    $.ajax({
        type: "GET",
        url: `${url}/tags/listar`,
        contentType: "application/json;charset=UTF-8",
        headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            $.map(data.content, function (tag) {
                $('#area_atuacao').append($('<option>', {
                    value: tag.id,
                    text: tag.nome
                }));
            });

            // VErificar pq não tá setando as opções	
            if (tags != null && tags.length > 0) {
                $.map(tags, function (tag) {
                    $("#area_atuacao")
                        .find(tag.id)
                        .prop("selected", true);
                });
            }

            $("#area_atuacao").trigger("chosen:updated");
        },
        error: function (data) {
            toastr.error('Erro: ' + data.responseJSON.erro);
        }
    });
}

function cadastrarVaga() {
    let titulo = $("#titulo").val();
    let descricao = $("#descricao").val();
    let salario = $(this).is(':checked') ? 'A Combinar' : $("#salario").val();
    let areaAtuacao = $("#area_atuacao").val();
    let institucional = 'INTERNO';
    let token = localStorage.getItem('token');
    let link = $("#link").val() ?? '';
    let tags = '';
    tags += "[";

    appUtil.showLoader('Cadastrando...');

    for (let i = 0; i < areaAtuacao.length; i++) {
        if (i + 1 == areaAtuacao.length) {
            tags += JSON.stringify({ id: areaAtuacao[i] });
            tags += "]";
        } else {
            tags += JSON.stringify({ id: areaAtuacao[i] }) + ", ";
        }
    }

    let newTags = JSON.parse(tags);

    $.ajax({
        type: "POST",
        url: `${url}/vaga/criar`,
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify({
            "titulo": titulo,
            "descricao": descricao,
            "salario": salario,
            "link": link,
            "tags": newTags,
            "expiracao": "11-11-2023 12:53",
            "institucional": institucional
        }),
        headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            $('#modalCadastroVaga').modal('hide');
            $('#data_table_vagas').DataTable();
            listar();
            toastr.options.closeButton = true;
            toastr.options.closeMethod = 'fadeOut';
            toastr.options.preventDuplicates = true;
            toastr.options.progressBar = true;
            toastr.success("Vaga cadastrada com sucesso!", 'Sucesso', { timeOut: 5000 });
        },
        error: function (data) {
            toastr.options.closeButton = true;
            toastr.options.closeMethod = 'fadeOut';
            toastr.options.preventDuplicates = true;
            toastr.options.progressBar = true;
            toastr.error(data.responseJSON.erro, 'Error', { timeOut: 5000 });
        },
        always: function () {
            appUtil.hideLoader();
        }
    });
}

function excluir(id) {
    let token = localStorage.getItem('token');

    // Falta verificar o motivo de n poder excluir
    // tb o X do modal confirm estar alinhado a esquerda
    appUtil.confirmBox('<h4 class="confirmModalCss">Deseja realmente excluir essa vaga???</h4>', function (retorno) {
        if (retorno) {
            appUtil.showLoader();
            $.ajax({
                type: "DELETE",
                url: `${url}/vaga/deletar/${id}`,
                headers: {
                    'Authorization': 'Bearer ' + token
                },
                success: function (data) {
                    appUtil.toastr("success", "Registro excluído com sucesso!", "Sucesso");
                    listar();
                },
                error: function (data) {
                    console.log(data);
                    appUtil.toastr("error", "Erro ao tentar excluir o registro.", "Error");
                },
                always: function () {
                    appUtil.hideLoader();
                }
            });

        }
    });
}


function suspender(id) {
    let token = localStorage.getItem('token');

    appUtil.confirmBox('<h4 class="confirmModalCss">Deseja realmente suspender essa vaga?</h4>', function (retorno) {
        if (retorno) {
            appUtil.showLoader('Suspendendo...');
            $.ajax({
                type: "PUT",
                url: `${url}/vaga/status/${id}`,
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify({
                    "status": "SUSPENSA",
                }),
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                success: function (data) {
                    appUtil.toastr("success", "Registro suspenso com sucesso!", "Sucesso");
                    listar();
                },
                error: function (data) {
                    console.log(data);
                    appUtil.toastr("error", "Erro ao tentar inativar o registro.", "Erro");
                },
                always: function () {
                    appUtil.hideLoader();
                }
            });

        }
    });
}
function candidatar(id) {
    let token = localStorage.getItem('token');

    appUtil.confirmBox('<h4 class="confirmModalCss">Deseja se candidatar para esta vaga?</h4>', function (retorno) {
        if (retorno) {
            appUtil.showLoader('Candidatando-se...');
            appUtil.toastr("warning", "Canditaturas em impementação!", "Atenção");
            // $.ajax({
            //     type: "PUT",
            //     url: `${url}/vaga/status/${id}`,
            //     contentType: "application/json;charset=UTF-8",
            //     data: JSON.stringify({
            //         "status": "SUSPENSA",
            //     }),
            //     headers: {
            //         'Accept': 'application/json, text/plain, */*',
            //         'Content-Type': 'application/json',
            //         'Authorization': 'Bearer ' + token
            //     },
            //     success: function (data) {
            //         appUtil.toastr("success", "Registro suspenso com sucesso!", "Sucesso");
            //         listar();
            //     },
            //     error: function (data) {
            //         console.log(data);
            //         appUtil.toastr("error", "Erro ao tentar inativar o registro.", "Erro");
            //     },
            //     always: function () {
            //         appUtil.hideLoader();
            //     }
            // });

        }
    });
}
function ativar(id) {
    let token = localStorage.getItem('token');

    appUtil.confirmBox('<h4 class="confirmModalCss">Deseja realmente ativar essa vaga?</h4>', function (retorno) {
        if (retorno) {
            appUtil.showLoader();
            $.ajax({
                type: "PUT",
                url: `${url}/vaga/status/${id}`,
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify({
                    "status": "ABERTA",
                }),
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                success: function (data) {
                    appUtil.toastr("success", "Registro ativado com sucesso!", "Sucesso");
                    listar();
                },
                error: function (data) {
                    console.log(data);
                    appUtil.toastr("error", "Erro ao tentar ativar o registro.", "Erro");
                },
                always: function () {
                    appUtil.hideLoader();
                }
            });

        }
    });
}

function editar(id) {
    let token = localStorage.getItem('token');

    appUtil.showLoader('Aguarde...');
    $.ajax({
        type: "GET",
        url: `${url}/vaga/pesquisar/${id}`,
        contentType: "application/json;charset=UTF-8",
        headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            carregarDadosModal(data);
        },
        error: function (data) {
            toastr.error('Erro: ' + data.responseJSON.erro);
            console.log(data);
        },
        always: function () {
            appUtil.hideLoader();
        }
    });
}

function carregarDadosModal(data) {
    appUtil.toastr("warning", "Em desenvolvimento!", "Atenção");

    limparCampos();

    $("#btnSalvar").html("Editar");
    $("#btnSalvar").val("Editar");


    $("#titulo").val(data.titulo);
    $("#descricao").val(data.descricao);
    $("#link").val(data.link);
    $("#id").val(data.id);
    $("#institucional").val(data.institucional);
    $("#dataCriacao").val(data.dataCriacao);

    if (data.salario) {
        $("#salario").val(data.salario);
        $(".salario").show();
    } else {
        $('#valorCombinar').prop('checked', true);
        $(".salario").hide();
    }

    carregarTags(data.tags);

    $('#modalCadastroVaga').modal('show');
}


//Continuar aqui
function atualizarVaga(data) {
    let token = localStorage.getItem('token');

    appUtil.toastr("error", "Em desenvolvimento!", "Erro");

    return;

    console.log('chegou', data);

    // $.ajax({
    //     type: "GET",
    //     url: `${url}/vaga/editar/${id}`,
    //     headers: {
    //         'Accept': 'application/json, text/plain, */*',
    //         'Content-Type': 'application/json',
    //         'Authorization': 'Bearer ' + token
    //     },
    //     success: function (data) {
    //         appUtil.toastr("success", "Registro atualizado com sucesso!", "Sucesso");
    //         listar();
    //     },
    //     error: function (data) {
    //         console.log(data);
    //         appUtil.toastr("error", data.responseJSON.erro, "Erro");
    //     }
    // });
}

function limparCampos() {
    $('#formCadastrarVaga').each(function () {
        this.reset();
    });
}

