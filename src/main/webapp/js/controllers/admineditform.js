app.controller("admineditformctrl", function($scope, $stateParams, myfactory,
		$state, $rootScope) {
	  $scope.user_name=localStorage.getItem("emplyeename");
	myfactory.get(TICKET_APP + "/tickets/attachment/" + $stateParams.ticketid)
			.then(function(data) {
				// var crtdate =data.createdDate;
				// console.log(crtdate);
				// document.getElementById(createdate).value=crtdate;
				console.log(data);
				$scope.ticketDetails = data;
				$rootScope.comment=$scope.ticketDetails.comment;
				$rootScope.assigndept=$scope.ticketDetails.assignTo;
				$scope.ticket_status= $scope.ticketDetails.status;
				if( $scope.ticket_status=="Assigned"){
					$scope.statuses  =["Assigned","WorkInProgress","Onhold","Pending","Pendingclarification"];	
				}
				 if( $scope.ticket_status=="Resolved"|| $scope.ticket_status=="Pendingclarification"|| $scope.ticket_status=="Closed"){
					 $("#submit_form").prop('disabled',true);
				 }
				
				if( $scope.ticket_status!=="Assigned"){
					$scope.statuses  = ["WorkInProgress","Onhold","Resolved","Pending","Pendingclarification"];	
				}
				if( $scope.ticket_status=="Resolved"){
					$scope.statuses  = ["Resolved","Closed"];	
				}
				console.log($scope.ticketDetails);
			}, function(err) {
				if (err) {
					$scope.errorMessage = err;
				}
			})

	// department api

	myfactory.get(TICKET_APP + "/tickets/dept").then(function(data) {
		console.log(data);
		$scope.department = data.body;
		console.log($scope.department);

	})
document.getElementById('clarification').addEventListener('keyup', function (event) {
		if(event.keyCode ==8&&document.getElementById('clarification').value==""){
			 $("#submit_form").prop('disabled',true);
		 }else{
			 $("#submit_form").prop('disabled',false);
		 }
	});
	$scope.reassignadmin=function(){
		if($scope.clarification==undefined){
			alert("pls fill give comments");
		}
		else{
			 var clarification=document.getElementById("clarification").value;
			 var d = new Date();
			 var current_date= d.toUTCString();
			 $scope.ticketDetails.comment="<"+$scope.user_name+">"+" "+current_date+"\n"+clarification+"\n"+$rootScope.comment;
			$("#reassignform").modal('show');
		}
	}
	 $scope.statusforuserfun = function(){
	    	var status = document.getElementById("stutus_user").value;
	    	document.getElementById('clarification').value="";
		if(status=="Resolved"|| status=="Pendingclarification"|| status=="Closed"){
			 $("#submit_form").prop('disabled',true);
		}
		else{
			 $("#submit_form").prop('disabled',false);
		}
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
					$scope.details = data;
				}, function(err) {
					swal("", "ticket is not raised server error", "error");
				});
	}
	
	$scope.sentbackfun=function(){
		 var query=document.getElementById("clarification").value;
		     $rootScope.query=query;
		  $('#querytext').val("");
		     $scope.addfields();
	
         
	}
	
	$scope.addfields=function(){
		var disc=$rootScope.description+"\n"+$rootScope.query;  
		   $rootScope.descp=disc;
		   $scope.querychangefun();
	}
/*	$scope.querychangefun=function(){
		 var id = $stateParams.ticketid;
			$scope.ticketDetails.id=id;
			  $scope.ticketDetails.description=$rootScope.descp;
			  var querydata={
					 "id": $scope.ticketDetails.id,
					  "description":$scope.ticketDetails.description
			  }
	myfactory.putdata(TICKET_APP +"/tickets/query/",querydata).then(
				function(data) {
					console.log(data);
					$scope.details = data;
				}, function(err) {
					
				});
			  
	}	*/
	
	
		$scope.updateticket = function() {

			 var clarification=document.getElementById("clarification").value;
			if( clarification==""){
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
						$state.transitionTo("raiseticket.assignedissues", null, {
							'reload' : true
						});
						console.log($scope.details);
					}, function(err) {
						
					});
			// document.ticketissue.reset();
	
				/*	 var clarification=document.getElementById("clarification").value;
					  $scope.ticketDetails.comment=clarification+$rootScope.comment;
					  var querydata={
								 "id": $scope.ticketDetails.id,
								  "comment":$scope.ticketDetails.comment
						  }
				myfactory.putdata(TICKET_APP +"/tickets/query/",querydata).then(
							function(data) {
								console.log(data);
								$scope.details = data;
								$state.transitionTo("raiseticket.assignedissues", null, {
									'reload' : true
								});
							}, function(err) {
								
							});*/
			  
		}
/*reassign with check box code*/
		
		$scope.reassign = function(){

		myfactory.get(TICKET_APP+"/department/email_by_dept/"+$scope.dept).then(function(data){
			console.log(data);
			$scope.assignedTo=data;
			console.log($scope.assignedTo);
		})
		}

$scope.reassignfun=function(){
/*	
			$scope.hidingstatus=true;
			$scope.sendbackclr=true;*/
			var formData = new FormData();
			var id = $stateParams.ticketid;
			var remark=$scope.remark;
			$scope.ticketDetails.id =id;
			$scope.ticketDetails.remark=remark;
			var params = $scope.ticketDetails;
			$.each(params, function(k, v) {
				formData.append(k, v);
			});
			myfactory.put(TICKET_APP +"/tickets/reassign/"+$scope.dept,formData).then(
					function(data) {
						$scope.details = data;
						console.log("data");
						swal("", "Ticket Re-assined Successfully", "success")
						$state.transitionTo("raiseticket.Dashboard", null, {
							'reload' : true
						});
						console.log($scope.details);
						$("#remarktext").val("");
						$("#deptid").val("");
						$("#reassignmail").val("");
					}, function(err) {
						
					});
	
			
				
			
}
$scope.assignfunemails=function(){
	myfactory.get(TICKET_APP+"/users/emails_within_department/"+$rootScope.assigndept).then(function(data){
		console.log(data);
		$scope.assignedTo=data.body;
		console.log($scope.assignedTo);
	})
}
		$scope.assignfun=function(){
			var formData = new FormData();
			var id = $stateParams.ticketid;
			$scope.ticketDetails.remark =$scope.remark;
			$scope.ticketDetails.id =id;
			$scope.ticketDetails.assignTo=$scope.assign;
			var params = $scope.ticketDetails;
			$.each(params, function(k, v) {
				formData.append(k, v);
			});
			myfactory.put(TICKET_APP +"/tickets/assign_within_department",formData).then(
					function(data) {
						$scope.details = data;
						console.log("data");
						swal("", "Ticket assined Successfully", "success")
						$state.transitionTo("raiseticket.Dashboard", null, {
							'reload' : true
						});
						console.log($scope.details);
						$("#remarktext").val("");
						$("#deptid").val("");
						$("#reassignmail").val("");
					}, function(err) {
						
					});
		
		}
	
	
	
})