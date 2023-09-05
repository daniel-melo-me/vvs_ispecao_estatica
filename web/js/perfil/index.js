jQuery(function () {
  $('#data_table_vagas').DataTable();
  $("#vagasRelacionadas").on("click", function () {
    vagasRelacionadas();
  });
  $("#btnAtualizar").on("click", function () {
    atualizarPerfil();
  });
  carregarDados();
  $("#curriculo").on('click', function () {
    cadastrarCurriculo();
  });
  $('#btn-curriculo').on('click', function() {
    
    appUtil.toastr("warning", "Em desenvolvimento!", "Atenção");

    return;
  });
  $('#curriculo').on('click', function() {
    appUtil.toastr("error", "Em desenvolvimento!", "Erro");

    return;
  });
});

getUsuario = () => {
  $.ajax({
    type: "GET",
    url: `${url}/usuario/getUsuario`,
    headers: {
      'Accept': 'application/json, text/plain, */*',
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token
    },
    success: function (data) {
        console.log("data.id " + data.id);
        return data.id;
    },
    error: function (data) {
      alert('Erro: ' + data.responseJSON.erro);
    },
    always: function () {
      appUtil.hideLoader();
    }
    
  })
}

function atualizarPerfil() {
  let token = localStorage.getItem('token');
  let nomePerfilAtualizar = $("#nomePerfilAtualizar").val();
  let emailPerfilAtualizar = $("#emailPerfilAtualizar").val();
  $.ajax({
    type: "PUT",
    url: `${url}/usuario/editar`,
    data: JSON.stringify({
      "nome": nomePerfilAtualizar,
      "email": emailPerfilAtualizar
    }),
    headers: {
      'Accept': 'application/json, text/plain, */*',
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token
    },
    success: function (data) {
      appUtil.toastr("success", "Registro atualizado com sucesso!", "Sucesso");
      carregarDados();
      location.reload();
    },
    error: function (data) {
      appUtil.toastr("error", data.responseJSON.erro, "Erro");
    },
    always: function () {
      appUtil.hideLoader();
    }
  })
}


function carregarDados() {
  let token = localStorage.getItem('token');

  $.ajax({
    type: "GET",
    url: `${url}/usuario/getUsuario`,
    headers: {
      'Accept': 'application/json, text/plain, */*',
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token
    },
    success: function (data) {
    console.log(data);

    let perfil = "";

    switch (data.perfis.nome) {
        case "ROLE_ADMIN": 
          perfil = "Administrador";
          break;
        case "ROLE_ALUNO":
          perfil = "Aluno";
          break;
        case "ROLE_PROFESSOR":
          perfil = "Professor";
          break;
        default:
          perfil = "Nenhum perfil atribuido";    
    }

    $("#nomePerfil").html(data.nome); 
    $("#dataCriacao").html(data.dataCriacao);
    $("#matricula").html(data.matricula);
    $("#emailPerfil").html(data.email);
    $("#perfil").html(perfil);
      
    },
    error: function (data) {
      alert('Erro: ' + data.responseJSON.erro);
    },
    always: function () {
      appUtil.hideLoader();
    }
    
  })
  
}

function vagasRelacionadas() {
  let token = localStorage.getItem('token');
  let html = "";

  $.ajax({
    type: "GET",
    url: `${url}/vaga/listar`,
    headers: {
      'Accept': 'application/json, text/plain, */*',
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token
    },
    success: function (data) {
    data.content.forEach(function (item) {

      if (item.salario == "") {
        item.salario = "A combinar";
    }

      html += ` <tr class="trCss">`;
      html += `     <td>${item.titulo}</td>`;
      html += `     <td>${item.descricao}</td>`;
      html += `     <td>${item.institucional}</td>`;
      html += `     <td>${item.dataCriacao}</td>`;
      html += `     <td>${item.status}</td>`;
      html += `     <td>${item.salario}</td>`;
      html += ` </tr>`;

  });

      $('#data_table_vagas').DataTable().destroy();
      $('#data_table_vagas tbody').html(html);
      $('#data_table_vagas').DataTable();

    },
    error: function (data) {
      alert('Erro: ' + data.responseJSON.erro);
    },
    always: function () {
      appUtil.hideLoader();
    }
    
  })

}

function cadastrarCurriculo() {
  let titulo = $("#titulo").val();
  let descricao = $("#descricao").val();
  appUtil.showLoader('Cadastrando...');

  $.ajax({
      type: "POST",
      url: `${url}/curriculo/criar`,
      contentType: "application/json;charset=UTF-8",
      data: JSON.stringify({
          "titulo": titulo,
          "descricao": descricao
      }),
      headers: {
          'Accept': 'application/json, text/plain, */*',
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token
      },
      success: function (data) {
          carregarDados()
          toastr.options.closeButton = true;
          toastr.options.closeMethod = 'fadeOut';
          toastr.options.preventDuplicates = true;
          toastr.options.progressBar = true;
          toastr.success("Currículo cadastrado com sucesso!", 'Sucesso', { timeOut: 5000 });
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

function abrirModalCurriculo() {
  appUtil.toastr("warning", "Em desenvolvimento!", "Atenção");

  return;
}