/*

This is frontend -stuff so it's not really part of the project. This is mainly to showcase that
the backend actually works. So sorry, this isn't really nice and clean code here 

*/

function setScreenshot(ssID) {
	const ssDiv = document.getElementById('screenshot');
	ssDiv.innerHTML = "<img src=\"test/test.png\" />";
}

function setRating(ssRating, id, name, myRating) {
	const ratingDiv = document.getElementById('rating');
	ratingDiv.innerHTML = "" + ssRating + " / 5";
	// also set name for the screenshot
	const rateDiv = document.getElementById('rate');
	rateDiv.innerHTML = "<p>" + name + "</p><p id=\"rateButtons\"></p>";
	createRateButtons(id, myRating);
}

function createRateButtons(id, myRating) {
	for (let i = 1; i <= 5; i++) {
		createRatingButton(id, i, myRating);
	}
}

function setComments(ssComments) {
	const commentDiv = document.getElementById('comments');
	for (let i = 0; i < ssComments.length; i++) {
		commentDiv.innerHTML += "<p class=\"comment\"><span class=\"cUser\">" + ssComments[i].user.username + "</span> " + ssComments[i].comment + "</p>";
	}
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

async function showScreenshot(id, loggedIn) {
	if (id <= 0) {
		await setRandom();
	}
	else {
		await setWithId(id);
	}
	if (loggedIn) {
		document.getElementById('rate').style.display = 'block';
		document.getElementById('comments').style.display = 'block';
	}
}

async function setWithId(id) {
	const screenshotData = await getData("/api/screenshots/" + id);
	const userRating = await getData("/api/ratings/" + id);
	setScreenshot(0);
	setRating(screenshotData.rating, screenshotData.id, screenshotData.name, userRating.rating);
	setComments(screenshotData.comments);
}

async function setRandom() {
	const screenshotData = await getData("/api/screenshots/random");
	const userRating = await getData("/api/ratings/" + screenshotData.id);
	setScreenshot(0);
	setRating(screenshotData.rating, screenshotData.id, screenshotData.name, userRating.rating);
	setComments(screenshotData.comments);
}

async function getData(url) {
	try {
		const response = await fetch(url);
		const responseJson = await response.json();
		return responseJson;
	} catch (error) {
		console.log(error);
		return null;
	}
}