'use strict';

/* Controllers */
var app = angular.module('app');
app.controller('logConfig', ['$scope', '$http', '$window','$modal', function ($scope, $http, $window,$modal) {


    $scope.logConfigs = [];

    $scope.pageChanged = function (pageNum,className) {
        var url = "/logger/get";
        $http.post(url, {}).success(function (data) {
            $scope.logConfigs = data;
        });
    }
    $scope.pageChanged(0);

    $scope.openLogConfig = function (logConfig) {
        var modalInstance = $modal.open({
            templateUrl: 'addLogConfigModel',
            backdrop: 'static',//空白处不关闭.
            controller: 'AddLogConfigModelCtrl',
            size:'lg',
            resolve: {
                items: function () {
                    return logConfig;
                }
            }
        });

        modalInstance.result.then(function (selectedItem) {
            $scope.pageChanged(0);
        }, function () {
            console.log('Modal dismissed at: ' + new Date());
        });
    };

    $scope.delete = function (config) {
        if(!$window.confirm("确认进行此次操作")){
            return;
        }

        $http.post("/logger/delete", config).success(function (data) {
            if (data.ok){
                alert("删除成功");
                $scope.pageChanged(0);
            }else {
                alert("删除失败"+data.resone);
            }
        });
    };

}]);


app.controller('AddLogConfigModelCtrl', ['$scope', '$http', '$modalInstance', 'items', function ($scope, $http, $modalInstance, items) {
    $scope.items = items;

    $scope.confirm = function () {
        $http.post("/logger/edit", $scope.items).success(function (data) {
            if (data.ok) {
                $modalInstance.close($scope.items);
            } else {
                alert(data.resone);
            }
        });
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
}]);