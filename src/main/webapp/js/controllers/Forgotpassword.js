angular.module("exampleApp").controller('forgotpwdctrl',function($scope,myfactory,$state){
	

    $scope.forgotpwd=function(){ 
   
       console.log($scope.forgtform);
	
			myfactory.posting(TICKET_APP+"/users/get_email",$scope.forgtform).then(function(data){
		   swal("", "OTP sent to your email address", "success")
           $state.go("newpwd");
        },function(err){
        	swal("", "Please Enter Registered Emailid", "error")
        	$state.transitionTo("forgotpwdpage",null,{reload:true})
            
        });
    }

});