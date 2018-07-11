app.controller("feedbackctrl",function($scope,myfactory,$rootScope,$state){
	 $scope.all_role=localStorage.getItem("roles");
	 if($scope.all_role=="ADMIN" && $scope.all_role!="USER"){
		 $scope.adminfeedback=true;
	 }
	 if($scope.all_role=="USER" && $scope.all_role!="ADMIN"){
		 $scope.userfeedback=true;
	 }	
	$scope.User_id= localStorage.getItem("usr_id");
	$scope.maxSize = 5;     // Limit number for pagination display number.  
    $scope.totalCount = 0;  // Total number of items in all pages. initialize as a zero  
    $scope.pageIndex = 1;   // Current page number. First page is 1.-->  
    $scope.pageSizeSelected = 5; // Maximum number of items per page. 
    $scope.doneissuesfun = function () {
    	$scope.stat="status";
    	$scope.status="Closed"
    	  myfactory.get(TICKET_APP+"/tickets/columnFilter/"+$scope.stat+"/"+$scope.status+"/"+$scope.User_id+"/" + $scope.pageIndex+ "/" + $scope.pageSizeSelected).then(  
                       function (response) {  
                    	   console.log(response);
                    	     $scope.totalCount = response.count; 
                            	if(response.errorCode=="OK"){
                         		$scope.tickets=true;
                         	}
                             	else if(response.errorCode=="NO_CONTENT"){
                         		$scope.notickets=true;
                         	}                      
                           $scope.doneissues = response.result;
                          $scope.totalCount = response.count;  
                       },  
                       function (err) {  
                           var error = err;  
                       });    	 
    }  
    //Loading  list on first time  
    //This method is calling from pagination number  
    $scope.pageChanged = function () {  
        $scope.doneissuesfun();  
    };    
    $scope.feedbackfun = function(id){ 
    	$rootScope.tickets_id = id;
    	}
    	$scope.maxRating = 5;
    	$scope.ratingNo = 0;
    	$scope.rateBy = function (star) {
    	$scope.ratingNo = star;
    $scope.rating={
    	  "ratingNo":$scope.ratingNo,
    	  "comment":$scope.comments
    	}
    	}
    	$scope.rateClick = function() { 
    	debugger;
    	myfactory.posting(TICKET_APP+"/rating/save/"+$rootScope.tickets_id,$scope.rating).then(function(data){
    		console.log(data);
    		swal("","raing submitted succesfully","success");
    		$scope.ratingNo="";
    	}), 
        function (err) {
    		$scope.ratingNo="";
            var error = err;  
        }; 
    	}
    
    	$scope.feedback = function(id){
    	myfactory.get(TICKET_APP+"/rating/"+id).then(function(data){
    		console.log(data);
    		$scope.feedback_admin=data.ratingNo;
    	})
    	}
    	
    	$scope.getYellowStars = function (num) {
    		var numberOfStars = Math.round(num);
    		if (numberOfStars > 5) 
    		numberOfStars = 5; 
    		var data = new Array(numberOfStars); 
    		for (var i = 0; i < data.length; i++) { 
    		data[i] = i; 
    		} 
    		return data; 
    		}
    		$scope.getGreyStars = function (num) { 
    		var numberOfStars = Math.round(num); 
    		var restStars = 5 - numberOfStars; 
    		if (restStars > 0) { 
    		var data = new Array(restStars); 
    		for (var i = 0; i < data.length; i++) { 
    		data[i] = i; 
    		} 
    		return data; 
    		} 
    		} 
    
    $scope.viewimage=function(id){
		myfactory.get(TICKET_APP+"/tickets/attachment/"+id ).then(  
			    function (response) { 
			                   
               },  
			                  function (err) {  
			             var error = err;  
			      });
  }
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

})