angular.module("exampleApp").controller('newpwdctrl',function($scope,myfactory,$state){
 $scope.newpasswd=function(){ 
   
       console.log($scope.newpwd);
	
			myfactory.posting(TICKET_APP+"/users/forgot_password",$scope.newpwd).then(function(data){
				if(data.status=="SUCCESS")
			{
			    swal("", "password changed succesfully", "success");
		
			    $state.go("/login");
			}
				
				if(data.status=="ERROR")
					{
		        	   swal("", "Password and Confirm password must be same", "error");
		        	   $state.transitionTo("newpwd",null,{'reload':true});
					}
				if(data.status=="Invalid OTP")
				{
	        	   swal("", "Invalid OTP", "error");
	        	   $state.transitionTo("newpwd",null,{'reload':true});
				}
        },function(err){
        	if(data.status=="ERROR")
	        	   swal("", "you entered wrong opt", "error");
	        	   $state.transitionTo("newpwd",null,{'reload':true});
        });
    }

});