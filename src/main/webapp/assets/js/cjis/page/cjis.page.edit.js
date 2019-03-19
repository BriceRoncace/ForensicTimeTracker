/* global cjis */

(function($, cjis) {
  cjis.page.edit = (function() {
    function settingsModal() {
      toggleCategories($.cookie('selectedCategories') ? JSON.parse($.cookie('selectedCategories')) : $('#catSelectModal').find(':checkbox').map(mapCheckedValues).get());

      $('#selectCategories').click(function() {
        var categories = JSON.parse($.cookie('selectedCategories'));

        $.each($('#catSelectModal').find(':checkbox'), function(i, el) {
          $(el).prop('checked', false);
        });

        $.each(categories, function(i, cat) {
          $('#toggle-' + cat + '').prop('checked', true);
        });

        $('#catSelectModal').modal('show');
      });

      $('#checkAllCategories').click(function() {
        $(this).closest('.modal-body').find(':checkbox').prop('checked', true);
      });

      $('#uncheckAllCategories').click(function() {
        $(this).closest('.modal-body').find(':checkbox').prop('checked', false);
      });

      $('#updateCategories').click(function() {
        var categories = $(this).closest('.modal').find(':checked').map(mapCheckedValues).get();
        if (categories.length !== 0) {
          toggleCategories(categories);
          $('#catSelectModal').modal('hide');
        }
      });

      function toggleCategories(categories) {
        $.each($('[data-entry]'), function(i, el) {
          $('[data-hidden-entries="' + $(el).data('entry') + '"]').append($(el));
        });

        $.each(categories, function(x, cat) {
          $.each($('[data-category="' + cat + '"]'), function(y, el) {
            if (x % 2 === 0) {
              $($('[data-entry-column="' + $(el).data('entry') + '"]')[0]).append($(el));
            }
            else {
              $($('[data-entry-column="' + $(el).data('entry') + '"]')[1]).append($(el));
            }
          });
        });
        $.cookie('selectedCategories', JSON.stringify(categories), {expires: 30});
      }

      function mapCheckedValues(i, el) {
        return $(el).val();
      }
    }

    function addRemoveEntries() {
      $('[data-add-entry]').click(function() {
        $(this).data('add-entry', addEntry($(this).data('add-entry')));
      });

      $(document).on('click', '[data-remove="discipline"]', function() {
        if ($('[data-remove="discipline"]:visible').length > 1) {
          removeEntry($(this));
        }
      });

      $(document).on('click', '[data-remove="court"]', function() {
        removeEntry($(this));
      });

      function removeEntry($removeBtn) {
        $removeBtn.closest('.card').addClass('d-none').find(':input').val('').trigger('change').removeAttr('required');
      }

      function addEntry(entry) {
        var $clone = $('#' + entry.name + '-clone').clone();
        $clone.removeAttr('id');
        $clone.removeClass('d-none');
        $clone.html($clone.html().replace(/{indexHere}/g, entry.index));
        $('#' + entry.name + '-zone').append($clone);
        $clone.find('[data-lab-case-number]').cjisLabCaseNumber();
        $clone.find('[data-toggle="popover"]').popover({trigger: 'hover'});
        return {name: entry.name, index: entry.index + 1};
      }
    }

    function redirectOnCookie() {
      if ($.cookie('returnQueryString')) {
        $('#workdayForm').append($('<input type="hidden" name="returnQueryString">').val($.cookie('returnQueryString')));
        $.removeCookie('returnQueryString', {path: cjis.context});
      }
    }

    function hourPicker() {
      $('body').popover({
        selector: '[data-hour-selector]',
        html: true,
        placement: 'left',
        title: getTitle,
        content: getContent
      }).on('show.bs.popover', function() {
        closeAll();
      });

      $(document).on('click', ':input:not(.calc-btn)', function() {
        closeAll();
      });

      function getTitle() {
        var $titleHtml = $('<div class="d-flex justify-content-between"><span>Select Hours</span></div>');
        var $button = $('<a href="javascript://nop" class="text-secondary"><i class="fas fa-times"></i></a>').click(closeAll);
        return $titleHtml.append($button);
      }

      function getContent() {
        var $input = $(this).siblings('input');
        var $hourPad = generateHourPad();
        var $minPad = generateMinPad();

        return $('<div class="d-inline-flex">').append($hourPad, $minPad);

        function generateMinPad() {
          var numBtns = [
            {value: '.00', action: input, hint: '0 mins'},
            {value: '.167', action: input, hint: '10 mins'},
            {value: '.25', action: input, hint: '15 mins'},
            {value: '.333', action: input, hint: '20 mins'},
            {value: '.50', action: input, hint: '30 mins'},
            {value: '.667', action: input, hint: '40 mins'},
            {value: '.75', action: input, hint: '45 mins'},
            {value: '.833', action: input, hint: '50 mins'}
          ];

          var $minPad = $('<div class="ml-2">');
          var $row = $('<div class="d-flex flex-row">');

          $.each(numBtns, function(i, numBtn) {
            if (i % 2 === 0) {
              $minPad.append($row);
              $row = $('<div class="d-flex flex-row">');
            }
            $row.append(getDefaultButton(numBtn.value, numBtn.hint).click(function() {
              numBtn.action(numBtn.value);
              closeAll();
              $input.trigger('change');
            }));
          });

          return $minPad.append($row);
        }

        function generateHourPad() {
          var numBtns = [
            {value: '1', action: input},
            {value: '2', action: input},
            {value: '3', action: input},
            {value: '4', action: input},
            {value: '5', action: input},
            {value: '6', action: input},
            {value: '7', action: input},
            {value: '8', action: input},
            {value: '9', action: input},
            {value: '<i class="far fa-times-circle" style="margin: 0 -3px;"></i>', action: clear, hint: 'clear'},
            {value: '0', action: input},
            {value: '<i class="fas fa-caret-left" style="width: 8px;"></i>', action: backspace, hint: 'bksp'}
          ];
          var $hourPad = $('<div class="w-70">');
          var $row = $('<div class="d-flex flex-row">');

          $.each(numBtns, function(i, numBtn) {
            if (i % 3 === 0) {
              $hourPad.append($row);
              $row = $('<div class="d-flex flex-row">');
            }
            $row.append(getDefaultButton(numBtn.value, numBtn.hint).click(function() {
              numBtn.action(numBtn.value);
              $input.trigger('blur');
            }));
          });

          return $hourPad.append($row);
        }

        function getDefaultButton(text, hint) {
          var $btn = $('<button type="button" class="btn btn-outline-primary grey-border calc-btn"></button>').html(text);
          if (hint) {
            var $hint = $('<div class="m-0 small text-muted text-center"></div>').html(hint);
            $btn.append($hint);
          }
          return $btn;
        }

        function input(val) {
          $input.val($input.val() + val);
        }

        function backspace() {
          $input.val($input.val().substring(0, ($input.val().length - 1)));
        }

        function clear() {
          $input.val('');
        }
      }

      function closeAll() {
        $('[data-hour-selector]').popover('hide');
      }
    }

    function cookieizeLabAndDiscipline() {
      $('[data-cookie-ize]').cookieizeValue();
    }

    function calculateTotals() {
      $(document).on('blur', '[data-addends]', function() {
        totalTime(['time', 'overtime']);
      });

      function totalTime(array) {
        $.each(array, function(i, name) {
          var total = 0;
          $.each($('[data-addends="' + name + '"]'), function(i, el) {
            total += Number($(el).val());
          });
          $('[data-sum="' + name + '"]').text(isNaN(total) ? 'Error!' : parseFloat(Math.round(total * 100) / 100).toFixed(2));
        });
      }
    }

    function labCaseNumber() {
      $('[data-lab-case-number]').cjisLabCaseNumber({
        labSelect: $('#lab')
      });
    }

    function confirmLeaveAfterChange() {
      $(document).on('change', '#workdayForm :input', function() {
        $(window).on('beforeunload', function() {
          return "Changes have been made to this day.";
        });
      });
    }

    function formValidation() {
      $('#workdayForm').on('submit', function(e) {
        if ($('#workdayForm')[0].checkValidity() === false) {
          e.preventDefault();
          e.stopPropagation();
        }
        $('#workdayForm').addClass('was-validated');
      });
    }

    function initPopovers() {
      $('[data-toggle="popover"]').popover({
        trigger: 'hover'
      });
    }

    return {
      settingsModal: settingsModal,
      addRemoveEntries: addRemoveEntries,
      redirectOnCookie: redirectOnCookie,
      hourPicker: hourPicker,
      cookieizeLabAndDiscipline: cookieizeLabAndDiscipline,
      calculateTotals: calculateTotals,
      labCaseNumber: labCaseNumber,
      confirmLeaveAfterChange: confirmLeaveAfterChange,
      formValidation: formValidation,
      initPopovers: initPopovers
    };
  })();
})(jQuery, cjis);