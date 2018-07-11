


app.controller("loginctrl",function($scope ,$state, UserService,$rootScope){

   $scope.rememberMe = false;
    $scope.login = function() {
//    	myfactory.posting(TICKET_APP+"/users/login",$scope.loginfrm).then(function(data){
//    		$scope.tkn=data.accessToken.token;
//    		$scope.role=data.roles;
//            $scope.details = data;
//            console.log($scope.details);
//            localStorage.setItem("roles",$scope.role);
//            localStorage.setItem("usertoken", $scope.tkn);
//            $state.go("raiseticket.viewtickets");
//             
//        },function(err){
//        	swal("", "Emailid or Password Incorrect", "error");
//        	$state.transitionTo("/login",null,{'reload':true});
//        });
    	
    	
    
    console.log($scope.email);
    console.log($scope.password);
    
    UserService.authenticate($.param({
        email: $scope.email,
        password: $scope.password
    }), function (authenticationResult) {
        
        console.log(authenticationResult);
        if(authenticationResult.status=="Un-autherized"){
        	swal("", "Emailid or Password Incorrect", "error");
       	$state.transitionTo("/",null,{'reload':true});
        }
    $scope.user_id= authenticationResult.accessToken.user.id;
    $scope.username= authenticationResult.accessToken.user.email;
    
    $scope.empid= authenticationResult.accessToken.user.empId;
    $scope.firstName= authenticationResult.accessToken.user.firstName;
    localStorage.setItem("employeeid", $scope.empid);
    localStorage.setItem("emplyeename", $scope.firstName);
    
    localStorage.setItem("usr_id", $scope.user_id);
    localStorage.setItem("usr_name",  $scope.username);
    
	$scope.role=authenticationResult.accessToken.user.roles;
    localStorage.setItem("roles",$scope.role);
    
 
    
        var accessToken = authenticationResult.accessToken.token;
        $rootScope.accessToken = accessToken;
        if ($scope.rememberMe) {
            $cookieStore.put('accessToken', accessToken);
        }
        UserService.get(function (user) {
            $rootScope.user = user;
            if($scope.role=="USER"){
            $state.go("raiseticket.Dashboard");
            }
            if($scope.role=="ADMIN"){
            	$state.go("raiseticket.Admindashboard");	
            }
//          
        });
    });
  document.frmv.reset();

  
  }
    
    

})