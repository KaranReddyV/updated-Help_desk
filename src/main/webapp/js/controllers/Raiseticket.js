app.controller('raisetcktctrl',function($state,$scope,myfactory,$rootScope){

	$scope.projects=["Ticket","Inventory","Resource Adda","Traffic App","Others"];
/*	$scope.issuetypes=["Hardware","Software","Others"];*/
	$scope.priorities=["Low","Medium","High"];
	$scope.statuses=["Opened","Pending","Closed","Resolved"];

	$scope.empId=  localStorage.getItem("employeeid");
	$scope.firstName=  localStorage.getItem("emplyeename");
    
    $scope.email_user=localStorage.getItem("usr_name");
	$scope.User_id= localStorage.getItem("usr_id");
	 $scope.all_role=localStorage.getItem("roles");
	 if($scope.all_role=="ADMIN" && $scope.all_role!="USER"){
		 $scope.all_users_role=true;
		 $scope.assignedissues=true;
	 }
	 if($scope.all_role=="USER" && $scope.all_role!="ADMIN"){
		 $scope.all_users_role=false;
		 $scope.updateticket=true;
	 }
		$("#sidenav").hide();	
	 $scope.togglefun=function(){
			$("#sidenav").toggle();	
		 }
 $scope.addNew=function(){
	 
		 console.log($rootScope.email1);
		 debugger;
	var formData = new FormData();
	var params = $scope.ticketDetails;
	$.each(params, function(k, v) {
	formData.append(k, v);
	});

	myfactory.post(TICKET_APP+"/tickets/create/"+$scope.User_id+"/"+$rootScope.email1,formData).then(function(data){
		       $scope.ticket_id=data.id;
	           $scope.details = data;
	         localStorage.setItem("Ticke_id",$scope.ticket_id);
	        	swal("", "Ticket Raised Successfully to this email"+"\n\n"+$rootScope.email1, "success");
	           $state.transitionTo("raiseticket.alltickets",null,{'reload':true});
	   	
	       },function(err){
	    		swal("", "ticket is not raised server error", "error");
	       });
	    document.ticketissue.reset();
	   }
 $scope.draftfun = function() {
		var formData = new FormData();
		var params = $scope.ticketDetails;
		$.each(params, function(k, v) {
			formData.append(k, v);
		});
		myfactory.post(
				TICKET_APP + "/tickets/draft/" + $scope.User_id + "/"
						+ $rootScope.email1, formData).then(
				function(data) {
					$scope.ticket_id = data.id;
				   	swal("", "Ticket saved as draft", "success");
					$scope.details = data;
				}, function(err) {
					swal("", "ticket is not raised server error", "error");
				});
	}
		$scope.clearform=function(){
			 document.ticketissue.reset();	
		}
		$scope.hrservicesfun=function(){
		    $scope.hr = "HR";
					myfactory.get(TICKET_APP+"/department/email_by_dept/"+$scope.hr).then(function(data){
						console.log(data);
					$scope.emailid=data.body.email;
					$rootScope.email1=$scope.emailid;
					$rootScope.issuetypes=data.body.issueList;
					console.log($scope.issuetypes);
					})
				
		}
	/*	$scope.salesservicesfun=function(){
		    $scope.sales ="Sales";
		    myfactory.get(TICKET_APP+"/department/email_by_dept/"+$scope.sales).then(function(data){
						console.log(data);
					$scope.emailid=data.body.email;
					$scope.issuetypes=data.body.issueList;
					$rootScope.email1=$scope.emailid;
					})
				
		}
*/
		$scope.Itservicesfun=function(){
		    $scope.It ="IT";
					myfactory.get(TICKET_APP+"/department/email_by_dept/"+$scope.It).then(function(data){
						console.log(data);
					$scope.emailid=data.body.email;
					$rootScope.email1=$scope.emailid;
					$rootScope.issuetypes=data.body.issueList;
					})
				
		}
		$scope.financialservicesfun=function(){
		    $scope.finance ="Finance";
					myfactory.get(TICKET_APP+"/department/email_by_dept/"+$scope.finance).then(function(data){
						console.log(data);
					$scope.emailid=data.body.email;
					$rootScope.issuetypes=data.body.issueList;
					$rootScope.email1=$scope.emailid;
					})
				
		}

		$scope.managementservicesfun=function(){
		    $scope.managementfun ="Management";
					myfactory.get(TICKET_APP+"/department/email_by_dept/"+$scope.managementfun).then(function(data){
						console.log(data);
					$scope.emailid=data.body.email;
					$rootScope.email1=$scope.emailid;
					$rootScope.issuetypes=data.body.issueList;
					})
				
		}
		$scope.staffingservicesfun=function(){
		    $scope.staffing ="Staffing";
					myfactory.get(TICKET_APP+"/users/assign_to/"+$scope.staffing).then(function(data){
						console.log(data);
					$scope.emailid=data.body.email;
					$rootScope.email1=$scope.emailid;
					})
				
		}
		$("#reasonid").hide();
		$scope.priorityfun = function(){
			var priority =document.getElementById("priorityid").value;
			if(priority=="High"){
				$("#reasonid").show();
			}else{
				$("#reasonid").hide();
			}
		}
	
	/*	$scope.salesissues();	*/
	
});