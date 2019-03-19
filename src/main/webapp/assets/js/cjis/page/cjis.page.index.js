/* global cjis */
/* global moment */

(function($, cjis) {
  cjis.page.index = (function() {
    function initFullCalendar() {
      var calendarHeader = {
        left: 'title',
        center: '',
        right: ''
      };
      var calendarOptions = {
        eventBackgroundColor: '#593196',
        eventBorderColor: '#593196',
        eventTextColor: '#fff',
        businessHours: true,
        aspectRatio: 1.9,
        header: calendarHeader,
        events: getEvents,
        dayClick: loadWorkday,
        eventClick: loadWorkday,
        eventRender: modifyEvent
      };
      
      $('#calendar').fullCalendar(calendarOptions);
      var calendar = $('#calendar').fullCalendar('getCalendar');
      
      $('div.fc-header-toolbar').addClass('row d-flex align-items-end justify-content-between');
      $('div.fc-left').addClass('col-3');
      $('div.fc-right').addClass('col-6 font1Point3').text('Click on a day to enter time.');
      $('div.fc-center').addClass('col-3 d-flex justify-content-end').append(getButtons(calendar));
      
      
      $('#userChooser').change(function() {
        calendar.refetchEvents();
      });
      
      function getButtons(calendar) {
        var $previous = $('<button type="button" class="btn btn-sm btn-outline-primary"><i class="fas fa-chevron-left"></i></button>').click(function() {
          calendar.prev();
        });
        
        var $current = $('<button type="button" class="btn btn-sm btn-outline-primary"><i class="fas fa-calendar-alt"></i> Current</button>').click(function() {
          calendar.today();
        });
        
        var $next = $('<button type="button" class="btn btn-sm btn-outline-primary"><i class="fas fa-chevron-right"></i></button>').click(function() {
          calendar.next();
        });
        
        return $('<div class="btn-group">').append($previous, $current, $next);
      }

      function getEvents(start, end, timezone, callback) {
        var username = $('#userChooser').val();
        $.getJSON(cjis.context + '/json/workdays', {username: username, start: start.format("YYYY-MM-DD"), end: end.format("YYYY-MM-DD")}, function(data) {
          callback(data.events);
          $('#weekTotals').empty().append(getTotals(data.weekTotals));
        });
  
        function getTotals(weekTotals) {
          var $rows = [];
          var $weekRows = $('.fc-week.fc-row');
          
          $.each(weekTotals, function(i, total) {
            var $normal = $('<tr>').append($('<td>').text('Normal'), $('<td>').text(total.normal));
            var $overtime = $('<tr>').append($('<td>').text('Overtime'), $('<td style="width: 35%;">').text(total.overtime));
            var $time = $('<td>').append($('<span class="badge badge-primary w-100">').html($('<table class="week-totals"><tbody>').append($normal, $overtime)));
            $rows.push($('<tr>').append($time).css('height', $($weekRows[i]).height()));
          });
          
          return $rows;
        }
      }

      function loadWorkday(object) {
        var date;
        if (moment.isMoment(object)) {
          date = object;
        }
        if (moment.isMoment(object.start)) {
          date = object.start;
        }
        if (moment.isMoment(date)) {
          var username = $('#userChooser').val();
          window.location.href = cjis.context + '/load?username=' + username + '&workdayDate=' + date.format("YYYY-MM-DD");
        }
      }

      function modifyEvent(event, $element) {
        var $normal = $('<tr>').append($('<td>').text('Normal'), $('<td>').text(event.normal).css('width', '35%'));
        var $overtime = $('<tr>').append($('<td>').text('Overtime'), $('<td>').text(event.overtime).css('width', '35%'));
        $element.find('.fc-content').empty().append($('<table>').css('font-size', '.9em').append($('<tbody>').append($normal, $overtime)));
        $element.attr('title', event.title);
      }
    }
    
    return {
      initFullCalendar: initFullCalendar
    };
  })();
})(jQuery, cjis);