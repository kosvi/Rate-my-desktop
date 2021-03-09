/*

This is frontend -stuff so it's not really part of the project. This is mainly to showcase that
the backend actually works. So sorry, this isn't really nice and clean code here 

*/

function setScreenshot(ssID) {
	const ssDiv = document.getElementById('screenshot');
	ssDiv.innerHTML = "<img src=\"test/test.png\" style=\"max-width: 30%\" />";
}

function setRating(ssRating) {
	const ratingDiv = document.getElementById('rating');
	ratingDiv.innerHTML = "" + ssRating + " / 5";
}

function setComments(ssComments) {
	const commentDiv = document.getElementById('comments');
	for (let i = 0; i < ssComments.length; i++) {
		commentDiv.innerHTML += "<p class=\"comment\"><span class=\"cUse\">" + ssComments[i].user.username + "</span> " + ssComments[i].comment + "</p>";
	}
}

async function setRandom() {
	const jsonData = await getData("/api/screenshots/random");
	setRating(jsonData.rating);
	setComments(jsonData.comments);
}

async function getData(url) {
	try {
		const response = await fetch("/api/screenshots/random");
		const responseJson = await response.json();
		return responseJson;
	} catch (error) {
		console.log(error);
		return null;
	}
}