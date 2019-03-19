(function ($) {
  $.fn.cookieizeValue = function() {
    $.each(this, function(i, el) {
      if (!$(el).val()) {
        if ($.cookie($(el).data('cookie-ize'))) {
          $(el).val($.cookie($(el).data('cookie-ize')));
        }
      }

      $(el).on('change', function() {
        $.cookie($(el).data('cookie-ize'), $(el).val(), { expires : 30 });
      });
    });
    
    return this;
  };
})(jQuery);