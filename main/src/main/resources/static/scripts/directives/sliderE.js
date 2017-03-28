'use strict';

angular.module('jenkinshue').directive('slider', function() {
  return {
    restrict: 'E',
    link: function(scope, element, attrs) {

      var getValue = function() {
        if(attrs.ngModel.indexOf('.') > -1) {
          var all = attrs.ngModel.split('.');
          var tmp;
          for(var i = 0; i < all.length; i++) {
          // for(var i in all) {
            var attribute = all[i];
            if(!tmp) {
              tmp = scope[attribute];
            } else {
              tmp = tmp[attribute];
            }
          }
          return tmp;
        } else {
          return scope[attrs.ngModel];
        }
      };

      var setValue = function(value) {
        scope.$apply(function() {
          if(attrs.ngModel.indexOf('.') > -1) {
            var all = attrs.ngModel.split('.');
            var tmp;

            for(var i = 0; i < all.length; i++) {
            // for(var i in all) {
              var attribute = all[i];
              if(!tmp) {
                tmp = scope[attribute];
              } else {
                if(i < all.length - 1) {
                  tmp = tmp[attribute];
                } else { // for the last element (all.length - 1)
                  tmp[attribute] = value;
                }
              }
            }
          } else {
            scope[attrs.ngModel] = value;
          }
        });
      };

      element.slider({
        value: getValue(),
        min: parseInt(attrs.min),
        max: parseInt(attrs.max),
        step: parseFloat(attrs.step)
      });
      element.on({
        slideStop: function(event) {
          setValue(event.value);
        }
      });
    }
  };
});
