'use strict';

/* Controllers */
var app = angular.module('app');
app.controller('positionHistoryCtrl', ['$scope', '$http', '$window','$modal','$interval', function ($scope, $http, $window,$modal,$interval) {

    $scope.positionHistorys = [];
    /**
     * 分页 + 查询 + 表格数据
     */
    $scope.pageSize = 15;
    $scope.pageNum = 0;
    $scope.pageChanged = function (pageNum,positionHistory) {
        var url = "/spc/positionHistory/getAll";
        positionHistory = positionHistory || {};
        var params = {
            "pageSize": $scope.pageSize,
            "pageNum": pageNum,
            "search":positionHistory
        }
        $http.post(url, params).success(function (data) {
            $scope.positionHistorys = data.content;
            //getLastSize(data.content);
            $scope.bigTotalItems = data.totalElements;
            $scope.pageNumSkip = $scope.bigCurrentPage = pageNum;
            $scope.pages = data.totalPages;
        });
    }

    $scope.pageChanged(0);


}]);

