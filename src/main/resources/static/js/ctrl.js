'use strict';

/* Controllers */
var app = angular.module('app',['ui.router',"ui.bootstrap",'dateTimePicker']);
app.controller('AppCtrl', ['$scope', '$http', '$window','$modal', function ($scope, $http, $window,$modal) {




    $scope.jobs = [];
    /**
     * 分页 + 查询 + 表格数据
     */
    $scope.pageSize = 10;
    $scope.pageNum = 0;
    $scope.pageChanged = function (pageNum,jobInfo) {
        var url = "/spc/jobInfo/getAll";
        jobInfo = jobInfo || {};
        var params = {
            "pageSize": $scope.pageSize,
            "pageNum": pageNum,
            "search":jobInfo,
        }
        $http.post(url, params).success(function (data) {
            $scope.jobs = data.content;
            $scope.bigTotalItems = data.totalElements;
            $scope.pageNumSkip = $scope.bigCurrentPage = pageNum;
            $scope.pages = data.totalPages;
        });
    }
    $scope.pageChanged(0);

    $scope.configs = [];
    $scope.getConfigs = function () {

        var url = "/spc/jobConfig/getAll";
        $http.post(url, {}).success(function (data) {
            $scope.configs = data.content;
        });

    }


    $scope.open = function (user) {
        var modalInstance = $modal.open({
            templateUrl: 'addUserModel',
            backdrop: 'static',//空白处不关闭.
            controller: 'AddUserModelCtrl',
            resolve: {
                items: function () {
                    return user;
                }
            }
        });

        modalInstance.result.then(function (selectedItem) {
            $scope.getConfigs();
            $scope.pageChanged(0);
        }, function () {
            console.log('Modal dismissed at: ' + new Date());
        });
    };

    $scope.operate = function (type,config) {
        if(!$window.confirm("确认进行此次操作："+type)){
            return;
        }
        config.operate = type;
        config['operate'] = type;
        $http.post("/spc/jobConfig/operate", config).success(function (data) {
            if (data.res) {
                $scope.getConfigs();
                $scope.pageChanged(0);
            } else {
                alert(data.info.message);
            }
        });
    };


}]);


app.controller('AddUserModelCtrl', ['$scope', '$http', '$modalInstance', 'items', function ($scope, $http, $modalInstance, items) {
    $scope.items = items;


    $scope.createJob = function () {

        console.log();
        $http.post("/spc/jobConfig/add", $scope.items).success(function (data) {
            if (data.res) {
                $modalInstance.close($scope.items);
            } else {
                alert(data.info.message);
            }
        });
    };

    $scope.editJob = function () {
        $http.post("/spc/jobConfig/edit", $scope.items).success(function (data) {
            if (data.res) {
                $modalInstance.close($scope.items);
            } else {
                alert(data.info.message);
            }
        });
    };



    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
}]);
