jQuery(function () {
    $("#login").on('click', function () {
        login();
    });

    $(".form-control-input").on('keyup', function (e) {
        if (e.keyCode === 13) {
            login();
        }
    });
});

function login() {
    let matricula = $("#lemail").val();
    let senha = $("#lpassword").val();

    $.ajax({
        type: "POST",
        url: `${url}/auth/login`,
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify({ matricula: matricula, senha: senha }),
        headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json'
        },
        success: function (data) {

            if (data.token) {
                setarValores(data.token);
            }

            $("#lemail").val('');
            $("#lpassword").val('');
        },
        error: function (data) {
            alert('Erro: ' + data.responseJSON.erro);
            $("#lemail").val('');
            $("#lpassword").val('');
        }
    });
}

function setarValores(token) {
    $.ajax({
        type: "GET",
        url: `${url}/usuario/getUsuario`,
        headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            localStorage.setItem('perfilUsuario', data.perfis.nome);
            localStorage.setItem('usuarioId', data.id);
            localStorage.setItem('token', token);
            window.location.href = "../vagas/index.html";
            appUtil.toastr("success", "Logado com sucesso!", "Sucesso");
        },
        error: function (data) {
            alert('Erro: ' + data.responseJSON.erro);
        },
        always: function () {
            appUtil.hideLoader();
        }

    })
}