angular.module("exampleApp").controller('regctrl',function($scope,myfactory, $state){
	
	
	 myfactory.get(TICKET_APP+"/users/roles").then(  
             function (response) {
                $scope.users = response.body;
                             },  
             function (err) {  
                 var error = err;  
             });  
	
    $scope.regsubmit=function(){
    	var email=document.getElementById("email").value;
    	var re = /^\s*[\w\-\+_]+(\.[\w\-\+_]+)*\@[\w\-\+_]+\.[\w\-\+_]+(\.[\w\-\+_]+)*\s*$/;
        if (re.test(email)) {
            if (email.indexOf('@ojas-it.com', email.length - '@ojas-it.com'.length) !== -1) {
            	   console.log($scope.regfrm);
       			myfactory.posting(TICKET_APP+"/users/register",$scope.regfrm).then(function(data){
                   $scope.details = data;
                   console.log($scope.details);
                   swal("", "Registerd successfully", "success")
                   $state.go("/login");
                   if(data.status=="ERROR"){
                	 swal("", "Email Id Already Exists", "error")
                     $state.transitionTo("regpage",null,{'reload':true});  
                   }
                   
               },function(err){
               	swal("", "You Are Not Registered", "error")
               	$state.transitionTo("regpage",null,{'reload':true});
               });
            document.regform.reset();  
            } else {
                alert('Email must be a ojas-it e-mail address (your@ojas-it.com).');
            	swal("", "You Are Not Registered", "error")
               	$state.transitionTo("regpage",null,{'reload':true});
            }
        } else {
            alert('Not a valid e-mail address.');
        	swal("", "You Are Not Registered", "error")
           	$state.transitionTo("regpage",null,{'reload':true});
        }
    
    }
    
    /*myfactory.get(TICKET_APP+"/tickets/dept").then(function(data){
		$scope.department = data;
		console.log($scope.department);
	},function(err){
        if(err){
            $scope.errorMessage = err;
        }else{
            $scope.errorMessage = err;
        }
	})*/

    $scope.rolechange=function(){
    	  $scope.role=document.getElementById("userrole").value;
    	  if($scope.role=="USER"){
    		  $("#dept").hide();
    	  }
    	  if($scope.role=="ADMIN"){
    		  $("#dept").show();
    		  document.regform.reset();
    	  }
    	    console.log(document.getElementById("userrole").value);
	/*	console.log($scope.role);*/
		myfactory.get(TICKET_APP+"/department/departments").then(function(data){
			console.log(data);
		 $scope.department = data;
			console.log($scope.department);
		
		})
	}
});