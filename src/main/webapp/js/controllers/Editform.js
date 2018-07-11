app.controller("editformctrl", function($scope, $stateParams, myfactory,
		$state, $rootScope) {

	$scope.projects = [ "Ticket", "Inventory", "Resource Adda", "Traffic App",
			"Others" ];

	$scope.priorities = [ "Low", "Medium", "High" ];
	/*$scope.statuses = ["Pendingclarification","Closed","Reopen","Draft","Resolved","Pending"];*/

	 $scope.all_role=localStorage.getItem("roles");
	  $scope.user_name=localStorage.getItem("emplyeename");
	myfactory.get(TICKET_APP + "/tickets/attachment/" + $stateParams.ticketid)
			.then(function(data) {
				console.log(data);
				$scope.ticketDetails = data;
				$rootScope.description=$scope.ticketDetails.description;
				 $scope.ticket_status= $scope.ticketDetails.status;
				$rootScope.dept_issuetype=$scope.ticketDetails.assignTo;
					$rootScope.comment=$scope.ticketDetails.comment;
				 $("#stutus_user").prop('disabled', true);
				 if( $scope.ticket_status=="Resolved"){
					$("#stutus_user").prop('disabled', false);
						$scope.updatestatus = ["Closed","Reopen","Resolved"];	
						
					}

					 if( $scope.ticket_status=="Resolved"|| $scope.ticket_status=="Pendingclarification"|| $scope.ticket_status=="Closed"){
						 $("#submit_form").prop('disabled',true);
					 }
					
//				 if( $scope.ticket_status=="Resolved"&&$scope.clarification==undefined){
//				 $("#clarification").keydown(function(){
//					 
//						 $("#submit_form").prop('disabled',true);
//					 
//					});
//				 }
					if($scope.ticket_status!=="Resolved"){
						
						$scope.updatestatus = ["Assigned","Pendingclarification","WorkInProgress","Reopen","Draft","Pending"];
					}
				    if( $scope.ticket_status=="Draft"){
						myfactory.get(TICKET_APP+"/department/email_by_dept/"+$rootScope.dept_issuetype).then(function(data){
							console.log(data);
						$scope.issuetypes=data.body.issueList;
						console.log($scope.issuetypes);
						})
					}
			}, function(err) {
				if (err) {
					$scope.errorMessage = err;
				}
			})

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
					$scope.details = data;
				}, function(err) {
					swal("", "ticket is not raised server error", "error");
				});
	}
	
	
//	 $("#clarification").keypress(function(){
//		 
//		 if($scope.clarification==""&&event.keyCode == 8){
//			 $("#submit_form").prop('disabled',true);
//		 }else{
//			 $("#submit_form").prop('disabled',false);
//		 }
//		 
//	 })
	$scope.updateticket = function() {		
		 var clarification=document.getElementById("clarification").value;
		 if(clarification!==""){
			 var d = new Date();
			 var current_date= d.toUTCString()
		  $scope.ticketDetails.comment="<"+$scope.user_name+">"+" "+current_date+"\n"+clarification+"\n"+$rootScope.comment;
		 }
		var id = $stateParams.ticketid;
		$scope.ticketDetails.id = id;
		console.log($scope.ticketDetails);
		var formData = new FormData();
		var params = $scope.ticketDetails;
		$.each(params, function(k, v) {
			formData.append(k, v);
		});
		$scope.email_user = localStorage.getItem("usr_name");
		myfactory.put(TICKET_APP + "/tickets/update/", formData).then(
				function(data) {
					$scope.details = data;
					swal("", "Ticket Updated Successfully", "success")
					$state.transitionTo("raiseticket.viewtickets", null, {
						'reload' : true
					});
					console.log($scope.details);
				}, function(err) {
					
				});
		// document.ticketissue.reset();
	}
	document.getElementById('clarification').addEventListener('keyup', function (event) {
		if(event.keyCode ==8&&document.getElementById('clarification').value==""){
			 $("#submit_form").prop('disabled',true);
		 }else{
			 $("#submit_form").prop('disabled',false);
		 }
	});
    $scope.statusforuserfun = function(){
    	var status = document.getElementById("stutus_user").value;
    	document.getElementById('clarification').value="";
	if(status=="Reopen"){
		 $('#reopen').modal('show');
	}
    }

    $scope.reopenfun = function(){
    	 $scope.ticketDetails.comment=$rootScope.comment+"\n"+"Reopen comment:"+$scope.reopencomment;
    	 var id = $stateParams.ticketid;
			$scope.ticketDetails.id = id;
			console.log($scope.ticketDetails);
			var formData = new FormData();
			var params = $scope.ticketDetails;
			$.each(params, function(k, v) {
				formData.append(k, v);
			});
			myfactory.put(TICKET_APP + "/tickets/reopen/", formData).then(
					function(data) {
						$scope.details = data;
						/*$state.transitionTo("raiseticket.assignedissues", null, {
							'reload' : true
						});*/
						console.log($scope.details);
					}, function(err) {
						
					});
		}
		
/*		$scope.statusforuserfun=function(){
				var user_status =document.getElementById("userstatus").value;
				if(user_status=="Closed"){
					$scope.updateticket = function() {
						var id = $stateParams.ticketid;
						$scope.ticketDetails.id = id;
						console.log($scope.ticketDetails);
						var formData = new FormData();
						var params = $scope.ticketDetails;
						$.each(params, function(k, v) {
							formData.append(k, v);
						});
						$scope.email_user = localStorage.getItem("usr_name");
						myfactory.put(TICKET_APP + "/tickets/update/", formData).then(
								function(data) {
									$scope.details = data;
									swal("", "Ticket Updated Successfully", "success")
									$state.transitionTo("raiseticket.assignedissues", null, {
										'reload' : true
									});
									console.log($scope.details);
								}, function(err) {
									
								});
						// document.ticketissue.reset();
					}
					
				}else{
					
				}
			
		}*/
		
		
	
})