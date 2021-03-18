class Profile {
	static async updateScreenshots() {
		const screenshots = await this.fetchScreenshots();
		if (screenshots != null) {
			const ssDiv = document.getElementById("screenshotlist");
			ssDiv.innerHTML = "<p class=\"smallTitle\">Screenshots</p>";
			for (let i = 0; i < screenshots.length; i++) {
				ssDiv.innerHTML += "<p><span class=\"deleteScreenshotButton\" onclick=\"Profile.deleteScreenshot(" + screenshots[i].screenshotID + ")\">X</span> <a href=\"/" + screenshots[i].screenshotID + "\">" + screenshots[i].screenshotName + "</a></p>";
			}
		}
	}

	static async fetchScreenshots() {
		try {
			const response = await fetch("/api/user/screenshots");
			const responseJson = await response.json();
			return responseJson;
		} catch (error) {
			console.log(error);
			return null;
		}
	}

	static async deleteScreenshot(id) {
		try {
			const response = await fetch("/api/screenshots/" + id, {
				method: 'DELETE',
				headers: {
					'Content-type': 'Application/json',
				},
			});
			const responseJSON = await response.json();
			await this.updateScreenshots();
		} catch (error) {
			console.log(error);
			document.getElementById('screenshotlist').innerHTML = error;
		}
	}
}
