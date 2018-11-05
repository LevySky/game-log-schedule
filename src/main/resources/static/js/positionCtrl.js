'use strict';

/* Controllers */
var app = angular.module('app');
app.controller('positionCtrl', ['$scope', '$http', '$window','$modal','$interval', function ($scope, $http, $window,$modal,$interval) {

    $scope.positions = [];
    /**
     * 分页 + 查询 + 表格数据
     */
    $scope.pageSize = 15;
    $scope.pageNum = 0;
    $scope.pageChanged = function (pageNum,position) {
        var url = "/spc/position/getAll";
        position = position || {};
        var params = {
            "pageSize": $scope.pageSize,
            "pageNum": pageNum,
            "search":position
        }
        $http.post(url, params).success(function (data) {
            $scope.positions = data.content;
            //getLastSize(data.content);
            $scope.bigTotalItems = data.totalElements;
            $scope.pageNumSkip = $scope.bigCurrentPage = pageNum;
            $scope.pages = data.totalPages;
        });
    }

    $scope.pageChanged(0);


    $scope.edit = function (postion) {
        var url = "/spc/position/edit";

        if(!confirm("确认修改？？？")){
           return;
        }

        $http.post(url, postion).success(function (data) {
           if(data.ok){
               $scope.posShow = false;
               $scope.pageChanged(0);
           }else{
                alert("Error"+data.resone);
           }
        });
    }


}]);

