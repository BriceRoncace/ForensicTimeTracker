(function($) {
  $.fn.cjisSorter = function(options) {
    $.each(this, function(i, el) {
      var settings = getSettings($(el), options);

      if (settings.options.$form.is('form')) {
        $.each(settings.sort, function(x, sort) {
          settings.options.$form.append($('<input type="hidden"/>').attr('name', 'sort').val(sort.name + ',' + sort.direction));
        });
        
        $.each($(el).find('[data-sort-on]'), function(y, header) {
          initHeader($(header), settings);
        });
      }
      else {
        console.error('Could not find search form. Either put the table in the search form, or define the "target" option.');
      }
    });

    return this;
  };

  function initHeader($el, settings) {
    var toggleSort = {
      ASC: function(name) {
        return {name: name, direction: 'DESC'};
      },
      DESC: function(name) {
        return {name: name, direction: 'ASC'};
      }
    };
    var order = getOrder(settings.sort, $el.data('sort-on'));

    $el.css({'cursor': 'pointer', 'text-decoration': 'underline'}).addClass(settings.options.headerStyle).append(getIcon(order)).click(onHeaderClick);
    
    function onHeaderClick(e) {
      var sort = (order) ? toggleSort[order.direction]($el.data('sort-on')) : {name: $el.data('sort-on'), direction: 'ASC'};
      
      if (e.ctrlKey) {
        removeElement(order);
      }
      else if (e.shiftKey) {
        removeElement(order);
        settings.sort.push(sort);
      }
      else {
        settings.sort = [sort];
      }
      
      settings.options.$form.find('[name="sort"]').remove();
      settings.options.$form[0].reset();
      $.each(settings.sort, function(i, s) {
        settings.options.$form.append($('<input type="hidden"/>').attr('name', 'sort').val(s.name + ',' + s.direction));
      });
      if ($.cookie) {
        $.cookie('sorterDefaultSort', JSON.stringify(settings.sort), {expires: settings.options.cookieExpires});
      }
      settings.options.$form.submit();
  
      function removeElement(element) {
        var index = settings.sort.indexOf(element);
        if (index > -1) {
          settings.sort.splice(index, 1);
        }
      }
    }
    
    function getOrder(sort, name) {
      var order;
      $.each(sort, function(i, s) {
        if (s.name === name) {
          order = s;
        }
      });
      return order;
    }
  
    function getIcon(order) {
      var iconBuilder = (function() {
        function buildIcon(style) {
          if (style !== '') {
            if (settings.options.showSortNumber) {
              return [$('<i>').addClass(style), $('<sup>').css('margin-left', '1px').text(settings.sort.indexOf(order) + 1)];
            }
            else {
              return $('<i>').addClass(style);
            }
          }
          else {
            return '';
          }
        }
        
        return {
          ASC: buildIcon(settings.options.ascStyle),
          DESC: buildIcon(settings.options.descStyle)
        };
      })();
      
      return (order) ? iconBuilder[order.direction] : '';
    }
  }

  function getSettings($el, userOptions) {
    var sort = parseSort($.extend({}, userOptions, $el.data()));
    var options = parseOptions($.extend({}, userOptions, $el.data()));
    
    return {sort: sort, options: options};

    function parseOptions(userOptions) {
      var newOptions = {};
      newOptions.$form = getStringValue(userOptions.target, '') !== '' ? $(userOptions.target) : $el.closest('form');
      newOptions.ascStyle = getStringValue(userOptions.ascStyle, 'small fas fa-chevron-down ml-1');
      newOptions.descStyle = getStringValue(userOptions.descStyle, 'small fas fa-chevron-up ml-1');
      newOptions.headerStyle = getStringValue(userOptions.headerStyle, 'text-primary');
      newOptions.cookieExpires = getNumberValue(userOptions.cookieExpires, 60);
      newOptions.showSortNumber = getShowSortNumberValue(userOptions.showSortNumber, sort.length > 1);
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

      function getNumberValue(userInput, defaultValue) {
        if (typeof userInput === 'string') {
          userInput = parseInt(userInput);
        }
        if (typeof userInput === 'number' && !isNaN(userInput)) {
          return userInput;
        }
        return defaultValue;
      }
      
      function getShowSortNumberValue(userInput, defaultValue) {
        if (typeof userInput === 'boolean') {
          return userInput;
        }
        if (typeof userInput === 'string') {
          if (userInput === 'true') {
            return true;
          }
          if (userInput === 'false') {
            return false;
          }
          if (userInput.match('^count > [0-9]{1,}$')) {
            return sort.length > parseInt(userInput.replace('count > ', ''));
          }
        }
        
        return defaultValue;
      }
    }

    function parseSort(userOptions) {
      var sort = getSort();
      
      var newSort = [];
      
      if (typeof sort === 'string') {
        var sortArray = sort.split(',');
        $.each(sortArray, function(i, stringOrder) {
          var orderArray = stringOrder.split(': ');
          newSort[i] = { name: orderArray[0], direction: orderArray[1] };
        });
      }
      
      if (Array.isArray(sort)) {
        $.each(sort, function(i, orderObject) {
          if (typeof orderObject.name === 'string' && typeof orderObject.direction === 'string') {
            newSort.push({ name: orderObject.name, direction: orderObject.direction });
          }
        });
      }
      
      return newSort;
      
      function getSort() {
        if (userOptions.sort) {
          return userOptions.sort;
        }
        if ($.cookie && $.cookie('sorterDefaultSort')) {
          return JSON.parse($.cookie('sorterDefaultSort'));
        }
        if (userOptions.defaultSort) {
          return userOptions.defaultSort;
        }
        return [];
      }
    }
  }
})(jQuery);