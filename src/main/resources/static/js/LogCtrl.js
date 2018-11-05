'use strict';

/* Controllers */
var app = angular.module('app');
app.controller('logTask', ['$scope', '$http', '$window','$modal', function ($scope, $http, $window,$modal) {
    $scope.logs = [];
    $scope.pageChanged = function (pageNum) {
        var url = "/spc/log/getAll";
        $http.post(url, {}).success(function (data) {
            $scope.logs = data;
        });
    }
    $scope.pageChanged(0);

    $scope.openLog = function (user) {
        var modalInstance = $modal.open({
            templateUrl: 'addLogModel',
            backdrop: 'static',//空白处不关闭.
            controller: 'AddLogModelCtrl',
            size:'lg',
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

    $scope.delete = function (config) {
        if(!$window.confirm("确认进行此次操作：")){
            return;
        }

        $http.post("/spc/log/delete", config).success(function (data) {
            if (data.ok) {
                $scope.pageChanged(0);
            } else {
                alert("Error"+data.resone);
            }
        });
    };

    $scope.execute = function (config) {
        if(!$window.confirm("确认进行此次操作：")){
            return;
        }
        $("#mask").show();
        $("#maskInfo").text("日志恢复，");
        $http.post("/spc/log/exec", config).success(function (data) {
            if (data.ok) {
                alert("OK!");
            } else {
                alert("Error "+data.resone);
            }
            $("#mask").hide();
        });
    };

    $scope.reloadServerList = function () {
        $http.post("/usercenter/reload", {}).success(function (data) {
            if (data.ok) {
                alert("OK!");
            } else {
                alert("Error "+data.resone);
            }
        });
    }




}]);


app.controller('AddLogModelCtrl', ['$scope', '$http', '$modalInstance', 'items', function ($scope, $http, $modalInstance, items) {
    $scope.items = items;


    $scope.logFileNames = []
    $scope.loadLogFileNames = function(){
        $http.post("/logger/getFileNames",{}).success(function (data) {
            $scope.logFileNames = data;
        });
    }
    $scope.loadLogFileNames();

    $scope.serverConfigs = [];
    $scope.loadServerConfig = function(){
        $http.post("/usercenter/getServerConfigs",{}).success(function (data) {
            $scope.serverConfigs = data;
        });
    }
    $scope.loadServerConfig();

    $scope.createlog = function () {
        $http.post("/spc/log/edit", $scope.items).success(function (data) {
            if (data.ok) {
                $modalInstance.close($scope.items);
            } else {
                alert(data.resone);
            }
        });
    };

    $scope.select = function(now,obj){

        obj = obj||{};
        if(obj[now] == null){
            obj[now] = now;
        }else{
            delete obj[now];
        }
        console.log(obj)
    }


    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.getSelected = function () {
        var values = [];
        $(".opTypeValue .active").each(function (pos, obj) {
            values.push($(obj).attr("value"));
        });

        return JSON.stringify(values).replace("[", '').replace("]", '')
            .replace(/\"/g, '');
    }

    $scope.arr = {};
    $scope.selectLogNames = function (selected) {

        if($scope.arr[selected]){
            delete $scope.arr[selected];
        }else{
            $scope.arr[selected] = selected;
        }
        var str = "";
        if($scope.arr['all']){
            $scope.arr = {all:selected};
            return 'all';
        }
        $.each($scope.arr,function (k,v) {
            str += k +",";
        });
        str = str.length > 0 ? str.substr(0,str.length-1):str;
        return str;
    }

}]);



app.filter('searchServer', function () {
    return function (input, query) {
        var out = [];
        if (!query)
            return input;
        angular.forEach(input, function (server) {
            var sid = "" + server.id;
            var sname = server.serverName;
            if (sid.indexOf(query) > -1 || sname.indexOf(query) > -1) {
                out.push(server);
            }
        });
        return out;
    }
});