($ => {
    $(() => {
        $('input[name=institucional]').on('change', function() {
            
            if ($(this).val() == 'false') {
                $('#link').attr('disabled', false);
            }
            else {
                $('#link').attr('disabled', true);
            }
        })
    });
})(jQuery);