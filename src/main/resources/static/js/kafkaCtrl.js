'use strict';

/* Controllers */
var app = angular.module('app');
app.controller('kafkaCtrl', ['$scope', '$http', '$window','$modal','$interval', function ($scope, $http, $window,$modal,$interval) {

    $scope.kafkas = [];
    /**
     * 分页 + 查询 + 表格数据
     */
    $scope.pageSize = 15;
    $scope.pageNum = 0;
    $scope.pageChanged = function (pageNum,kafka) {
        var url = "/spc/kafka/getAll";
        kafka = kafka || {};
        var params = {
            "pageSize": $scope.pageSize,
            "pageNum": pageNum,
            "topic":kafka.topic||'',
            "partition":kafka.partition||''
        }
        $http.post(url, params).success(function (data) {
            $scope.kafkas = data.content;
            //getLastSize(data.content);
            $scope.bigTotalItems = data.totalElements;
            $scope.pageNumSkip = $scope.bigCurrentPage = pageNum;
            $scope.pages = data.totalPages;
        });
    }

    var getLastSize = function(list){

         var _list = angular.copy(list);

         for(var i  in list){
             var time = new Date(list[i].updateTime);
             for(var j in _list){
                 var _time = new Date(_list[j].updateTime);
                 var d = time.getTime() - _time.getTime() ;
                 if(list[i].topic == _list[j].topic && list[i].partition == _list[j].partition && d>0 && d < 20000 ){
                     list[i]["value"] = list[i].offset - _list[j].offset;
                     break;
                 }
             }
             list[i]["value"] = list[i]["value"] || 0;
         }
         //console.log(list);
    }

    $scope.pageChanged(0);


    // var timer=$interval(function(){
    //     $scope.pageChanged($scope.pageNumSkip,$scope.kafka);
    // },5000);
    //
    // timer.then(function(){ console.log('创建成功')},
    //     function(){ console.log('创建不成功')});

}]);

