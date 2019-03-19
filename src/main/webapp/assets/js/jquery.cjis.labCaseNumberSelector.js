(function($) {
  $.fn.cjisLabCaseNumber = function(options) {
    
    var settings = $.extend({
      labSelect: ''
    }, options );

    $.each($(this).filter('input:visible'), function(i, el) {
      setLabNumberPlaceholder();
      
      $(el).parent().append(buildButtonBar($(el)));
      
      $(settings.labSelect).on('change', function() {
        setLabNumberPlaceholder();
      });
      
      $(this).focus(function() {
        makePlaceholderReal($(this));
      });
      
      $(this).blur(function() {
        ifNoChangeGoBackToPlaceholder($(this));
      });
    });
    return this;

    function buildButtonBar($input) {
      var thisYear = new Date().getFullYear();
      var labList = [{funct: setLab, text: 'C'}, {funct: setLab, text: 'M'}, {funct: setLab, text: 'P'}];
      var yearList = [{funct: setYear, text: (thisYear - 3).toString().substring(2)}, {funct: setYear, text: (thisYear - 2).toString().substring(2)},
                      {funct: setYear, text: (thisYear - 1).toString().substring(2)}, {funct: setYear, text: thisYear.toString().substring(2)}];
      var numbList = [{funct: setNumber, text: '0'}, {funct: setNumber, text: '1'}, 
                      {funct: setNumber, text: '2'}, {funct: setNumber, text: '3'}, 
                      {funct: setNumber, text: '4'}, {funct: setNumber, text: '5'}, 
                      {funct: setNumber, text: '6'}, {funct: setNumber, text: '7'}, 
                      {funct: setNumber, text: '8'}, {funct: setNumber, text: '9'}, 
                      {funct: setNumber, text: '<i class="fas fa-caret-left"></i>'}];

      var $buttonBar = $('<div class="row align-items-center justify-content-center">');
      return $buttonBar.append(createButtonRow($('<div class="btn-group">'), $input, labList), 
                               createButtonRow($('<div class="btn-group ml-3">'), $input, yearList), 
                               createButtonRow($('<div class="btn-group ml-3">'), $input, numbList)
                              );
    }

    function createButtonRow($wrapper, $input, list) {
      $.each(list, function(i, el) {
        $wrapper.append(buildButton(el.text).click(function() {
          el.funct($input, $(this));
        }));
      });
      return $wrapper;
    }

    function buildButton(text) {
      return $('<button type="button" tabindex="-1" class="btn btn-xs btn-outline-primary px-2 grey-border"></button>').html(text);
    }
    
    function makePlaceholderReal($this) {
      var labInfo = parseLabNumber('');
      if ($this.val() === '') {
        $this.val(labInfo.lab + labInfo.year + '-');
      }
    }
    
    function ifNoChangeGoBackToPlaceholder($this) {
      var labInfo = parseLabNumber('');
      if ($this.val() === (labInfo.lab + labInfo.year + '-')) {
        $this.val('');
      }
    }

    function setLabNumberPlaceholder() {
      var labNumber = $('[data-lab-case-number]:visible');
      if (labNumber) {
        var labInfo = parseLabNumber('');
        $.each($('[data-lab-case-number]'), function() {
          $(this).attr('placeholder', (labInfo.lab + labInfo.year + '-'));
        });
      }
    };

    function setLab($input, $this) {
      var labNumber = parseLabNumber($input.val());
      $input.val($this.html() + labNumber.year + '-' + labNumber.number);
      $input.trigger('change');
    };

    function setYear($input, $this) {
      var labNumber = parseLabNumber($input.val());
      $input.val(labNumber.lab + '20' + $this.html() + '-' + labNumber.number);
      $input.trigger('change');
    };

    function setNumber($input, $this) {
      var newNumber = $this.html();
      var labNumber = parseLabNumber($input.val());
      var existNumber = labNumber.number;
      if (newNumber !== '0' && !Number(newNumber)) {
        if (existNumber.length > 0) {
          existNumber = existNumber.substring(0, existNumber.length - 1);
        }
      }
      else {
        if (existNumber.length > 3) {
          existNumber = existNumber.substring(1, 4) + newNumber;
        }
        else {
          existNumber = existNumber + newNumber;
        }
      }
      $input.val(labNumber.lab + labNumber.year + '-' + existNumber);
      $input.trigger('change');
    };

    function parseLabNumber(existText) {
      var lab = 'M';
      if ($(settings.labSelect).val()) {
        lab = $(settings.labSelect).val().charAt(0);
        if (lab === 'H') {
          lab = 'M';
        }
      }
      var year = new Date().getFullYear();
      var number = '';
      var test;
      if (existText !== '' || existText !== null) {
        if ((existText.charAt(0).toUpperCase() === 'C') || (existText.charAt(0).toUpperCase() === 'M') || (existText.charAt(0).toUpperCase() === 'P')) {
          lab = existText.charAt(0).toUpperCase();
          test = existText.substring(1);
        }
        else {
          test = existText;
        }
        if (test.length >= 4 && Number(test.substring(0, 4)) >= 2000 && Number(test.substring(0, 4)) <= 2030) {
          year = test.substring(0, 4);
          number = test.substring(4);
        }
        else {
          number = test;
        }
        if (number.substring(0, 1) === '-') {
          number = number.substring(1);
        }
      }
      return {
        lab: lab,
        year: year,
        number: number };
    };
  };
})(jQuery);