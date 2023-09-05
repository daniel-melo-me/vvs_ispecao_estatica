/**
 * Objeto|Classe de utilidades
 * @type object
 */
var appUtil = {
    /**
     * Mensagens do sistema ( Erro, Sucesso, Infomação )
     * @param {string} message
     * @param {string} target
     * @param {string} tipo
     * @returns {undefined}
     */
    createFlashMessager: function (message, tipo, target) {
        var css = "";
        var icon = "";
        if (tipo == "success" || tipo === true || tipo == "true") {
            css = "success";
            icon = "check";
        }

        if (tipo == "danger" || tipo === false || tipo == "false") {
            css = "danger";
            icon = "remove";
        }

        if (tipo == "info") {
            css = "info";
            icon = "info";
        }

        if (appValidation.isUndefined(target)) {
            target = "#flashMessager";
        }

        var container = $(target);

        container.append(
            $.parseHTML(
                '<div class="alert alert-' +
                css +
                '">' +
                '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>' +
                '<i class="fa fa-' +
                icon +
                '"></i> ' +
                message +
                "</div>"
            )
        );

        var position = container.position();

        window.scrollTo(parseInt(position.top), 0);
        appUtil.limparFlashMesseger(5000);
    },

    /**
     * Ajusta o confirm para padronizar os botões
     * @param {string} message
     * @param {function} callBackFunction
     * @returns {undefined}
     */
    confirmBox: function (message, callBackFunction) {
        bootbox.confirm({
            message: message,
            closeButton: false,
            size: 'large',
            buttons: {
                confirm: {
                    label: 'Sim',
                    className: 'btn-success'
                },
                cancel: {
                    label: 'Não',
                    className: 'btn-danger'
                }
            },
            callback: callBackFunction
        });
    },
    /**
     * Padroniza os alerts
     * @param {string} type
     * @param {string} msg
     * @returns {string} title
     */
    toastr: function (type, msg, title) {
        toastr.options.closeButton = true;
        toastr.options.closeMethod = 'fadeOut';
        toastr.options.preventDuplicates = true;
        toastr.options.progressBar = true;

        if (type == "success") {
            toastr.success(msg, title, { timeOut: 5000 });
        }

        if (type == "error") {
            toastr.error(msg, title, { timeOut: 5000 });
        }

        if (type == "info") {
            toastr.info(msg, title, { timeOut: 5000 });
        }

        if (type == "warning") {
            toastr.warning(msg, title, { timeOut: 5000 });
        }
    },
    /**
     * Limpa o cookie por nome
     * @param {string} name
     * @returns {undefined}
     */
    limparCookie: function (name) {
        createCookie(name, "", -1);
    },

    /**
     * Função para fazer o sistema mimi
     * @param {integer} delay
     * @returns {undefined}
     */
    sleep: function (delay) {
        var start = new Date().getTime();
        while (new Date().getTime() < start + delay);
    },
    limparFlashMesseger: function (time) {
        var tempo = time ? time : 20000;
        $(".alert").delay(tempo).fadeOut();
        appUtil.limparFlashMessegerClasseHasError(tempo);
    },

    limparFlashMessegerClasseHasError: function (tempo) {
        setTimeout(function () {
            $(".has-error").removeClass("has-error");
        }, tempo);
    },

    /**
     * Mostra o loader do sistema
     * @param {string} text
     */
    showLoader: function (text) {
        $(".loader-body").show();
        $(".loader-text").html(text);
    },

    /**
     * Esconde o loader do sistema
     * @param {string} text
     */
    hideLoader: function () {
        $(".loader-body").hide();
        $(".loader-text").html("");
    },
};
