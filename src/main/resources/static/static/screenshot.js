/*

This is frontend -stuff so it's not really part of the project. This is mainly to showcase that
the backend actually works. So sorry, this isn't really nice and clean code here 

*/

function setScreenshot(ssID) {
	document.getElementById('screenshotID').value = ssID;
	const ssDiv = document.getElementById('screenshot');
	ssDiv.innerHTML = "<img src=\"/pics/" + ssID + "\" />";
}

function setRating(ssRating, id, name, myRating) {
	const ratingDiv = document.getElementById('rating');
	if (ssRating > 0) {
		ratingDiv.innerHTML = "" + ssRating + " / 5";
	} else {
		ratingDiv.innerHTML = "not yet rated";
	}
	if (document.getElementById('nav').innerText.includes("ADMIN")) {
		ratingDiv.innerHTML += " <span class=\"deleteScreenshotButton\" onclick='deleteScreenshot(" + id + ")'>x</span>";
	}
	// also set name for the screenshot
	const titleField = document.getElementById('screenshotTitle');
	titleField.innerText = name;
	const rateDiv = document.getElementById('rate');
	//	rateDiv.innerHTML = "<p>" + name + "</p><p id=\"rateButtons\"></p>";
	rateDiv.innerHTML = "<p id=\"rateButtons\"></p>";
	createRateButtons(id, myRating);
}

function createRateButtons(id, myRating) {
	for (let i = 1; i <= 5; i++) {
		createRatingButton(id, i, myRating);
	}
}

function setComments(ssComments) {
	const commentDiv = document.getElementById('comments');
	commentDiv.innerHTML = "";
	for (let i = 0; i < ssComments.length; i++) {
		const date = new Date(ssComments[i].timestamp);
		var deleteButton = "";
		if (document.getElementById('nav').innerText.includes("ADMIN")) {
			deleteButton = "<span class=\"deleteScreenshotButton\" onclick='deleteComment(" + ssComments[i].commentID + ")'>x</span> ";
		}
		commentDiv.innerHTML += "<p class=\"comment\">" + deleteButton + makeTimestamp(date) + "<span class=\"cUser\">" + ssComments[i].user.username + "</span> " + ssComments[i].comment + "</p>";
	}
}

function makeTimestamp(date) {
	return date.getDate() + "." + (date.getMonth() + 1) + "." + date.getFullYear();
}

function createRatingButton(id, value, myValue) {
	var rateButton = document.createElement('span');
	rateButton.setAttribute('id', 'rate' + value);
	if (value === myValue) {
		rateButton.className = 'rateButton chosen';
	}
	else {
		rateButton.className = 'rateButton';
	}
	rateButton.innerHTML = value;
	rateButton.setAttribute('onclick', 'updateRating(' + id + ', ' + value + ')');
	document.getElementById('rateButtons').appendChild(rateButton);
}

async function updateRating(id, value) {
	const url = "/api/ratings/rate/" + id + "?newValue=" + value;
	const newRating = await getData(url);
	console.log(newRating);
	document.getElementById('rateButtons').innerHTML = "";
	createRateButtons(id, newRating.rating);
}

async function showScreenshot(id) {
	if (id <= 0) {
		await setRandom();
	}
	else {
		await setWithId(id);
	}
}

function displayElements(loggedIn) {
	if (loggedIn) {
		document.getElementById('rate').style.display = 'block';
		document.getElementById('comments').style.display = 'block';
		document.getElementById('newComment').style.display = 'block';
	}
}

async function setWithId(id) {
	const screenshotData = await getData("/api/screenshots/" + id);
	const userRating = await getData("/api/ratings/" + id);
	setScreenshot(id);
	var rating;
	if (userRating == null) {
		rating = -1;
	}
	else {
		rating = userRating.rating;
	}
	setRating(screenshotData.rating, screenshotData.id, screenshotData.name, rating);
	setComments(screenshotData.comments);
}

async function setRandom() {
	const screenshotData = await getData("/api/screenshots/random");
	const userRating = await getData("/api/ratings/" + screenshotData.id);
	setScreenshot(screenshotData.id);
	var rating;
	if (userRating == null) {
		rating = -1;
	}
	else {
		rating = userRating.rating;
	}
	setRating(screenshotData.rating, screenshotData.id, screenshotData.name, rating);
	setComments(screenshotData.comments);
}

async function getData(url) {
	console.log("Fetching url: " + url);
	try {
		const response = await fetch(url);
		const responseJson = await response.json();
		return responseJson;
	} catch (error) {
		console.log(error);
		return null;
	}
}

async function postData(url, body) {
	try {
		const response = await fetch(url, {
			method: 'POST',
			headers: {
				'Content-type': 'Application/json',
			},
			body: JSON.stringify(body),
		});
		const responseJson = await response.json();
		return responseJson;
	} catch (error) {
		console.log(error);
		return null;
	}
}

async function addComment() {
	const comment = document.getElementById('commentText').value;
	document.getElementById('commentText').value = "";
	if (comment.length < 2) {
		return;
	}
	const ssID = document.getElementById('screenshotID').value;
	var body = { "comment": comment };
	const comments = await postData("/api/comments/" + ssID, body);
	setComments(comments);
}


async function deleteScreenshot(id) {
	try {
		const response = await fetch("/api/screenshots/" + id, {
			method: 'DELETE',
			headers: {
				'Content-type': 'Application/json',
			},
		});
		const responseJSON = await response.json();
		console.log(responseJSON);
		document.getElementById("screenshotTitle").innerText = responseJSON.screenshotName;
	} catch (error) {
		console.log(error);
	}
}

async function deleteComment(id) {
	try {
		const response = await fetch("/api/comments/" + id, {
			method: 'DELETE',
			headers: {
				'Content-type': 'Application/json',
			},
		});
		const responseJSON = await response.json();
		console.log(responseJSON);
	} catch (error) {
		console.log(error);
	}
}


