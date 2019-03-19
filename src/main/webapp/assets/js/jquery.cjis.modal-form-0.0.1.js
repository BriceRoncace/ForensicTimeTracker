(function($) {
  $.fn.modalForm = function() {
    var $modal = $(this);
    
    $modal.find('[data-value]').each(function(i, el) {
      var $el = $(el);

      if ($el.is(':checkbox')) {
        $el.data('default-value', $el.is(':checked'));
        $el.data('add-value', function(value) {
          $el.prop('checked', value === true).trigger('change');
        });
        $el.data('reset-value', function() {
          $el.prop('checked', $el.data('default-value') === true).trigger('change');
        });
      }
      else if ($el.is(':input')) {
        $el.data('default-value', $el.val());
        $el.data('add-value', function(value) {
          $el.val(value).trigger('change');
        });
        $el.data('reset-value', function() {
          $el.val($el.data('default-value')).trigger('change');

          if ($el.hasClass("tt-input")) {
            $el.typeahead('val', $el.data('default-value'));
          }  
        });
      }
      else {
        $el.data('default-value', $el.text());
        $el.data('add-value', function(value) {
          $el.text(value);
        });
        $el.data('reset-value', function() {
          $el.text($el.data('default-value'));
        });
      }
    });
    
    var newClickSelector = $modal.data("new-selector");
    $(newClickSelector).click(showModalForm);
    
    var editClickSelector = $modal.data("edit-selector");
    $(editClickSelector).click(editModalForm);
    
    if ($modal.data("reset-on-hide") === true) {
      $modal.on('hidden.bs.modal', resetModalForm);
    }
    
    function showModalForm() {
      $modal.modal('show');
    }
    
    function resetModalForm() {
      $.each($modal.find('[data-value]'), function(i, el) {
        var $el = $(el);
        if (typeof $el.data('reset-value') === 'function') {
          $el.data('reset-value').call(el);
        }
      });

      $modal.find('.alert').alert('close');
    }
    
    function editModalForm() {
      var dataObj = $(this).data();

      $.each($modal.find('[data-value]'), function(i, el) {
        var $el = $(el);
        if (typeof $el.data('add-value') === 'function') {
          $el.data('add-value').call(el, dataObj[$el.data('value')]);
        }
      });
      $modal.modal('show');
    }

    return this;
  };  
}(jQuery));