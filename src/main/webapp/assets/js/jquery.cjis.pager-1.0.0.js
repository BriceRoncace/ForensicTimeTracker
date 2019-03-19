(function ($) {
  $.fn.cjisPager = function(options) {
    $.each(this, function(i, el) {
      var settings = getSettings($(el), options);
      
      if (settings.options.$form.is('form')) {
        if (settings.options.$form.find('[name="size"]').length === 0) {
          settings.options.$form.append($('<input type="hidden" name="size"/>').val(settings.page.size));
        }
        if (settings.isValid) {
          $(el).append(buildPager(settings));
        }
      }
      else {
        console.error('Could not find search form. Either put the element in the search form, or define the "target" option.');
      }
      
    });
    
    return this;
  };
  
  function buildPager(settings) {
    var $prevBtn = buildPrevBtn();
    var $pageDropdown = buildPageDropdown();
    var $resultsSpan = buildResultsSpan();
    var $sizeDropdown = buildSizeDropdown();
    var $centerDiv = $('<div class="form-inline col d-flex justify-content-around">').append($pageDropdown, $resultsSpan, $sizeDropdown);
    var $nextBtn = buildNextBtn();
    
    return $('<div class="col d-flex justify-content-between">').append($prevBtn, $centerDiv, $nextBtn);
      
    function buildSizeDropdown() {
      if (Array.isArray(settings.options.sizes) && settings.page.totalElements !== 0) {
        var $button = buildButton().attr('data-toggle', 'dropdown').addClass('dropdown-toggle');
        var $menu = $('<div class="dropdown-menu" style="max-height: 300px; overflow-y: auto; border-radius: 0;">');
        $.each(settings.options.sizes, function(i, size) {
          $menu.append(buildMenuItem(size));
        });
        
        return $('<div class="dropdown">').append($button, $menu);
      }
      
      function buildMenuItem(size) {
        var $item = $('<a href="javascript://nop" class="dropdown-item">').text(size);
        if (settings.page.size === size) {
          $item.addClass('active');
        }
        $item.click(function(e) {
          if ($.cookie) {
            $.cookie('pagerDefaultSize', size, {expires: settings.options.cookieExpires});
          }
          submitForm(0, size);
        });
        return $item;
      }
      
      function buildButton() {
        var $button = $('<button type="button" class="btn">').addClass(settings.options.btnStyle);
        return $button.text(settings.page.size + ' results per page');
      }
    }
    
    function buildResultsSpan() {
      if (settings.page.numberOfElements && settings.page.totalElements) {
        var startingNumber = settings.page.size * settings.page.number;
        var endingNumber = startingNumber + settings.page.numberOfElements;
        return $('<span>').addClass(settings.options.textStyle).text((startingNumber + 1) + ' - ' + endingNumber + ' of ' + settings.page.totalElements);
      }
    }
      
    function buildPageDropdown() {
      if (settings.options.showPages) {
        if (settings.page.totalPages > 1) {
          var $toggleBtn = $('<button type="button" class="btn dropdown-toggle">').addClass(settings.options.btnStyle).attr('data-toggle', 'dropdown');
          $toggleBtn.text('Page ' + (settings.page.number + 1) + ' of ' + (settings.page.totalPages));
          var $menu = $('<div class="dropdown-menu" style="max-height: 300px; overflow-y: auto; border-radius: 0;">');
          for(i = 0; i < settings.page.totalPages; i++) {
            $menu.append(buildMenuItem(i));
          }

          return $('<div class="dropdown">').append($toggleBtn, $menu);
        }
        else if (settings.page.totalPages === 1) {
          var $button = $('<button type="button" class="btn">').addClass(settings.options.btnStyle).prop('disabled', true);
          return $button.text('Page ' + (settings.page.number + 1) + ' of ' + (settings.page.totalPages));
        }
        else {
          var $button = $('<button type="button" class="btn">').addClass(settings.options.btnStyle).prop('disabled', true);
          return $button.text(settings.options.noResults);
        }
      }
      
      function buildMenuItem(number) {
        var $item = $('<a href="javascript://nop" class="dropdown-item">').text(number + 1);
        if (number === settings.page.number) {
          $item.addClass('active');
        }
        $item.click(function (e) {
          submitForm(number, settings.page.size);
        });
        return $item;
      }
    }
    
    function buildNextBtn() {
      if (settings.page.totalElements !== 0) {
        var $nextBtn = $('<button type="button" class="btn">Next <i class="fas fa-chevron-right"></i></button>').addClass(settings.options.btnStyle);
        if (settings.page.number >= settings.page.totalPages - 1) {
          $nextBtn.prop('disabled', true);
        }
        else {
          $nextBtn.click(function (e) {
            submitForm(settings.page.number + 1, settings.page.size);
          });
        }
        return $nextBtn;
      }
    }
    
    function buildPrevBtn() {
      var $prevBtn = $('<button type="button" class="btn"><i class="fas fa-chevron-left"></i> Previous</button>').addClass(settings.options.btnStyle);
      if (settings.page.number <= 0) {
        $prevBtn.prop('disabled', true);
      }
      else {
        $prevBtn.click(function (e) {
          submitForm(settings.page.number - 1, settings.page.size);
        });
      }
      return $prevBtn;
    }
    
    function submitForm(page, size) {
      settings.options.$form[0].reset();
      settings.options.$form.find('[name="size"]').val(size);
      settings.options.$form.append($('<input type="hidden" name="page"/>').val(page)).submit();
    }
  }
  
  function getSettings($el, userOptions) {
    var page = parsePageNumbers($.extend({}, userOptions, $el.data()));
    var options = parseOptions($.extend({}, userOptions, $el.data()));
    
    if (!page.size) {
      page.size = options.defaultSize;
    }
    
    return {page: page, options: options, isValid: isValidPage(page)};
    
    function isValidPage(page) {
      var isValid = true;
      if (!isValidNumber(page.number)) {
        console.warn('Need current page number. Please define the "pageNumber" option.');
        isValid = false;
      }
      if (!isValidNumber(page.totalPages)) {
        console.warn('Need total number of pages. Please define the "totalPages" option.');
        isValid = false;
      }
      return isValid;
    }
    
    function parseOptions(options) {
      var newOptions = {};
      newOptions.$form = getStringValue(options.target, '') !== '' ? $(options.target) : $el.closest('form');
      newOptions.btnStyle = getStringValue(options.btnStyle, 'btn-outline-primary');
      newOptions.textStyle = getStringValue(options.textStyle, 'text-primary');
      newOptions.showPages = typeof options.showPages === 'boolean' ? options.showPages : true;
      newOptions.sizes = Array.isArray(options.sizes) ? options.sizes : null;
      newOptions.defaultSize = getDefaultSize(Array.isArray(newOptions.sizes) ? newOptions.sizes[0] : options.defaultSize, 20);
      newOptions.noResults = getStringValue(options.noResults, 'No results');
      newOptions.cookieExpires = isValidNumber(options.cookieExpires) ? options.cookieExpires : 60;
      return newOptions;
      
      function getStringValue(userInput, defaultValue) {
        if (userInput === null) {
          userInput === '';
        }
        if (typeof userInput === 'string') {
          return userInput;
        }
        return defaultValue;
      }
      
      function getDefaultSize(userInput, defaultValue) {
        if ($.cookie && $.cookie('pagerDefaultSize')) {
          return $.cookie('pagerDefaultSize');
        }
        if (isValidNumber(userInput)) {
          return userInput;
        }
        return defaultValue;
      }
    }
    
    function parsePageNumbers(page) {
      var newPage = {};
      newPage.number = parseInt(page.pageNumber);
      newPage.totalPages = parseInt(page.totalPages);
      newPage.numberOfElements = parseInt(page.numberOfElements);
      newPage.totalElements = parseInt(page.totalElements);
      newPage.size = parseInt(page.pageSize);
      return newPage;
    }
  }
  
  function isValidNumber(number) {
    return typeof number === 'number' && !isNaN(number);
  }
})(jQuery);