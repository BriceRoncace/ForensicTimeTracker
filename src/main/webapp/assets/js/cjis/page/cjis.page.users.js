/* global cjis */

(function($, cjis) {
  cjis.page.users = (function() {
    function init() {
      $('#userModal').modalForm();  
    }
    
    return {
      init: init
    };
  })();
})(jQuery, cjis);