/* global cjis */

(function($, cjis) {
  cjis.page.search = (function() {
    function tableRowClick() {
      $('[data-admin-load]').click(function() {
        $.cookie('returnQueryString', window.location.search, {path: cjis.context});
        window.location.href = $(this).attr('data-admin-load');
      });
    }
    
    function exportClick() {
      $('#exportSearch').click(function () {
        var defaultUrl = $(this).closest('form').attr('action');
        $(this).closest('form').attr('action', cjis.context + '/admin/export').submit();
        $(this).closest('form').attr('action', defaultUrl);
      });
    }
    
    function cjisPager() {
      $('[data-page-number]').cjisPager({ btnStyle: 'btn-outline-primary btn-sm', sizes: [20, 50, 100] });
    }
    
    function cjisSorter() {
      $('[data-sort]').cjisSorter({defaultSort: [{name: 'lastName', direction: 'ASC'}, {name: 'workdayDate', direction: 'ASC'}]});
    }
    
    return {
      tableRowClick: tableRowClick,
      exportClick: exportClick,
      cjisPager: cjisPager,
      cjisSorter: cjisSorter
    };
  })();
})(jQuery, cjis);