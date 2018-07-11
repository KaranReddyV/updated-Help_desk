app.controller('viewticketctrl', function($state, $scope, myfactory,
		$stateParams,$rootScope) {
    $scope.gostate = function(  ){
        $state.go(raiseticket.checkstatus.viewtickets)
    }

	$scope.statuses=["All","Opened","Assigned","Draft","Pending","WorkInProgress","Pendingclarification","Resolved","Closed"];
	$scope.priorities=["Low","Medium","High"];
	$scope.maxSize = 5; // Limit number for pagination display number.
	$scope.totalCount = 0; // Total number of items in all pages. initialize as
	// a zero
	$scope.pageIndex = 1; // Current page number. First page is 1.-->
	$scope.pageSizeSelected = 5; // Maximum number of items per page.
	$scope.User_id = localStorage.getItem("usr_id")
	$scope.all_role = localStorage.getItem("roles");
	$scope.viewtickets = function() {
		$scope.isloading = true;
		if ($scope.all_role == "USER" && $scope.all_role != "ADMIN")
			myfactory.get(
					TICKET_APP + "/tickets/user/" + $scope.User_id + "/"
							+ $scope.pageIndex + "/" + $scope.pageSizeSelected)
					.then(function(response) {
						console.log(response);
						$scope.details = response.result; 
						$scope.totalCount = response.count;
						if (response.errorCode == "OK") {
							$scope.tickets = true;
						}
						else if (response.errorCode == "NO_CONTENT") {
							$scope.notickets = true;
						}
						$scope.isloading = false;
					}, function(err) {
						var error = err;
					});
		if ($scope.all_role == "ADMIN" && $scope.all_role != "USER")
			myfactory.get(
					TICKET_APP + "/tickets/"+$scope.User_id+"/"+ $scope.pageIndex + "/"
							+ $scope.pageSizeSelected).then(function(response) {
								console.log(response);
								console.log(response);
								$rootScope.assigndept=response.deptName;
								$scope.details = response.result;
							
				$scope.totalCount = response.count;
				if (response.errorCode == "OK") {
					$scope.tickets = true;
				}
				else if (response.errorCode == "NO_CONTENT") {
					$scope.notickets = true;
				}
				$scope.details = response.result;
				$scope.totalCount = response.count;
				$scope.isloading = false;
				myfactory.get(TICKET_APP+"/users/emails_within_department/"+$rootScope.assigndept).then(function(data){
					console.log(data);
					$scope.assignedTo=data.body;
					console.log($scope.assignedTo);
				})
			}, function(err) {
				var error = err;
			});		
	}
	
	 if($scope.all_role=="ADMIN" && $scope.all_role!="USER"){
		 $scope.all_users_role=false;
	 }
	 if($scope.all_role=="USER" && $scope.all_role!="ADMIN"){
		 $scope.all_users_role=true;
	 }
	// Loading list on first time
	// This method is calling from pagination number
	$scope.pageChanged = function() {
		$scope.viewtickets();
	};
	$scope.viewimage = function(id) {
		myfactory.get(TICKET_APP + "/tickets/attachment/" + id).then(
				function(response) {
					 $scope.attachment = response.attachment;
				}
                , function(err) {
                	
				});
	}
	
	$('#notickets').hide();
$scope.statuschnagefun=function(){
	$scope.columnstatus="status";
	myfactory.get( TICKET_APP+"/tickets/columnFilter/"+$scope.columnstatus+"/"+$scope.status+"/"+$scope.User_id+"/"+ $scope.pageIndex + "/"+ $scope.pageSizeSelected).then(
			function(response) {
			$scope.details =response.result;
			if(response.errorCode=="NO_CONTENT"){
				$('#listoftickets').hide();
				$('#notickets').show();
			}
			else{
				$scope.details =response.result;
				$('#listoftickets').show();
				$('#notickets').hide();
			}
			});
} 
$scope.prioritychnagefun=function(){
	$scope.coulumnpriority="priority";
	myfactory.get( TICKET_APP+"/tickets/columnFilter/"+$scope.coulumnpriority+"/"+$scope.priority+"/"+$scope.User_id+"/"+ $scope.pageIndex + "/"+ $scope.pageSizeSelected).then(
			function(response) {
			$scope.details =response.result;
			if(response.errorCode=="NO_CONTENT"){
				$('#listoftickets').hide();
				$('#notickets').show();
			}
			else{
				$scope.details =response.result;
				$('#listoftickets').show();
				$('#notickets').hide();
			}
			
			});
}
myfactory.get(TICKET_APP + "/tickets/getallticketid/"+$scope.User_id).then(
		function(response) {
			 $scope.ticketid = response.result;
		}
        , function(err) {
        	
		});
/*profile details*/
$scope.profilefun = function(id){
myfactory.get(TICKET_APP + "/tickets/attachment/" + id)
.then(function(data) {
	// var crtdate =data.createdDate;
	// console.log(crtdate);
	// document.getElementById(createdate).value=crtdate;
	console.log(data);
	$scope.ticketDetails = data;
	$rootScope.description=$scope.ticketDetails.description;
	console.log($scope.ticketDetails);
	console.log($scope.ticketDetails.fileName);
}, function(err) {
	if (err) {
		$scope.errorMessage = err;
	}
})
}

$scope.ticketidfun=function(){
	$scope.ticketsid="id";
	myfactory.get( TICKET_APP+"/tickets/columnFilter/"+$scope.ticketsid+"/"+$scope.listofids+"/"+$scope.User_id+"/"+ $scope.pageIndex + "/"+ $scope.pageSizeSelected).then(
			function(response) {
			$scope.details =response.result;
			if(response.errorCode=="NO_CONTENT"){
				$('#listoftickets').hide();
				$('#notickets').show();
			}
			else{
				$scope.details =response.result;
				$('#listoftickets').show();
				$('#notickets').hide();
			}
			
			});
}




$scope.assignfun=function(id,assignTo){
	myfactory.put(TICKET_APP +"/tickets/assign_within_department/"+id+"/"+assignTo).then(
			function(data) {
				$scope.details = data;
				console.log(data);
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
$scope.reverse=false;
	$('.fa-sort-desc').hide();
	$scope.orderByMe = function(col) {
	$scope.column = col;
	if($scope.reverse){
	   $scope.reverse = false;
	   $('.fa-sort-asc').show();
	   $('.fa-sort-desc').hide();
	   }else{
	   $scope.reverse = true;
	   $('.fa-sort-desc').show();
	   $('.fa-sort-asc').hide();
	   }
	}
});
