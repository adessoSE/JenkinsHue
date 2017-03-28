/*
 * fix for scrollbars
 */
$(document).ready(function() {
  var height = $(document).height() - 50;
  $('.content-wrapper').css({
   'min-height' : height,
  });
});
