'use strict';

angular.module('jenkinshue')
  .factory('paginationFactory', ['$http', '$timeout',
    function($http, $timeout) {

      var searchingVar;

      return {
        url: '/rest/users',
        count: 0,
        page: 0,
        lastPage: 0,
        pageSize: 10,
        searchItem: '',
        dtos: [],
        searching: false,
        setURL: function(u, that) {
          if(!that) {
            that = this;
          }
          that.dtos = [];
          that.url = u;
          that.refresh(that);
        },
        refresh: function(that) {
          if(!that) {
            that = this;
          }
          that.page = 0;
          that.updateCount(that);
          that.reloadDTOs(that);
        },
        updateCount: function(that) {
          if(!that) {
            that = this;
          }
          $http.get(that.url + '/count', {params: {searchItem: that.searchItem}}).success(function(data) {
            that.count = data;
            that.computeLastPage(that);
          });
        },
        computeLastPage: function(that) {
          if(!that) {
            that = this;
          }
          that.lastPage = Math.floor(that.count / that.pageSize);
          if((that.count % that.pageSize === 0) && (that.count > 0)) {
            that.lastPage -= 1;
          }
        },
        pageSizeHasChanged: function(that) {
          if(!that) {
            that = this;
          }
          that.computeLastPage(that);
          that.page = 0;
          that.reloadDTOs(that);
        },
        backPossible: function(that) {
          if(!that) {
            that = this;
          }
          return that.page > 0;
        },
        back: function(that) {
          if(!that) {
            that = this;
          }
          if(that.backPossible(that)) {
            that.page -= 1;
            that.reloadDTOs(that);
          }
        },
        forwardPossible: function(that) {
          if(!that) {
            that = this;
          }
          return that.page < that.lastPage;
        },
        forward: function(that) {
          if(!that) {
            that = this;
          }
          if(that.forwardPossible(that)) {
            that.page += 1;
            that.reloadDTOs(that);
          }
        },
        computeSearch: function(that) {
          if(!that) {
            that = this;
          }
          that.searching = true;

          $timeout.cancel(searchingVar);
          searchingVar = $timeout(that.refresh, 400, true, that);
        },
        reloadDTOs: function(that) {
          if(!that) {
            that = this;
          }
          $http.get(that.url + '/' + that.page + '/' + that.pageSize, {params: {searchItem: that.searchItem}})
            .success(function(data) {
              that.dtos = data;
              that.searching = false;
            })
            .error(function() {
              that.searching = false;
            });
        }
      }
    }]);
