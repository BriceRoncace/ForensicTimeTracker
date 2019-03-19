var cjis = (function() {
  
  $('[data-submit]').click(function(e) {
    var $form = $(this).closest('form');
    var action = $(this).data('submit');
    if (action !== '') {
      $form.attr('action', action);
    } 
    $form.submit();
  });
  
  return {
    page: {},
    context: window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1))
  };
})();