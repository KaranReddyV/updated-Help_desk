app.controller("ticketdetailsctrl", function($scope, $stateParams, myfactory,
		$state, $rootScope) {
	myfactory.get(TICKET_APP + "/tickets/attachment/" + $stateParams.ticketid)
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

})