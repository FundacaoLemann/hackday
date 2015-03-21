function school($scope, $http) {
    $http.get('http://localhost:8080/school').
        success(function(data) {
            $scope.schools = data;
        });
}
function student($scope, $http) {
    $http.get('http://localhost:8080/student').
        success(function(data) {
            $scope.students = data;
        });
}
