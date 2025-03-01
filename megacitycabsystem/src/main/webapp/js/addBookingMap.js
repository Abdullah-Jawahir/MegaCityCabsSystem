document.addEventListener("DOMContentLoaded", function () {
	// Reference to map and distance elements
	var distanceValueElement = document.getElementById("distanceValue");
	var distanceField = document.getElementById("distanceField");

	// Check if map exists (prevents errors if map element isn't loaded yet)
	if (!document.getElementById("map")) {
		console.error("Map element not found");
		return;
	}

	// Initialize map with custom styling
	var map = L.map("map", {
		zoomControl: false, // We'll use custom zoom controls
	}).setView([40.7128, -74.006], 13);

	// Add a colorful map tile layer - REPLACE CARTO LIGHT WITH MORE COLORFUL MAPBOX STYLE
	L.tileLayer("https://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png", {
		attribution:
			'&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, Tiles style by <a href="https://www.hotosm.org/" target="_blank">HOT</a>',
		maxZoom: 19,
	}).addTo(map);

	// Custom markers with improved styling
	var pickupIcon = L.divIcon({
		html: '<div class="custom-marker pickup-marker"><i class="fas fa-dot-circle"></i></div>',
		className: "",
		iconSize: [40, 40],
		iconAnchor: [20, 20],
	});

	var dropIcon = L.divIcon({
		html: '<div class="custom-marker drop-marker"><i class="fas fa-map-pin"></i></div>',
		className: "",
		iconSize: [40, 40],
		iconAnchor: [20, 40],
	});

	// Markers for pickup and drop locations
	var pickupMarker = null;
	var dropMarker = null;
	var routeLine = null;

	// Enhanced function to calculate and display distance
	function calculateDistance() {
		console.log("Calculating distance...");
		if (pickupMarker && dropMarker) {
			var pickup = pickupMarker.getLatLng();
			var drop = dropMarker.getLatLng();

			// Remove previous route
			if (routeLine) {
				map.removeLayer(routeLine);
			}

			// Create enhanced curved route line
			var latlngs = [pickup];

			// Calculate control points for a more natural curve
			var midPoint = L.latLng(
				(pickup.lat + drop.lat) / 2,
				(pickup.lng + drop.lng) / 2
			);

			// Calculate perpendicular offset for curve control point
			var dx = drop.lng - pickup.lng;
			var dy = drop.lat - pickup.lat;
			var dist = Math.sqrt(dx * dx + dy * dy);

			// Adjust curve based on distance
			var curveFactor =
				Math.min(0.2, 0.5 / dist) * (Math.random() * 0.5 + 0.75);

			// Create control point perpendicular to the direct path
			var offsetX = -dy * curveFactor;
			var offsetY = dx * curveFactor;

			var ctrlPoint = L.latLng(midPoint.lat + offsetY, midPoint.lng + offsetX);

			// Create several points along the bezier curve for smoother arc
			var steps = 20;
			for (var i = 1; i <= steps; i++) {
				var t = i / steps;

				// Quadratic bezier formula
				var lat =
					Math.pow(1 - t, 2) * pickup.lat +
					2 * (1 - t) * t * ctrlPoint.lat +
					Math.pow(t, 2) * drop.lat;

				var lng =
					Math.pow(1 - t, 2) * pickup.lng +
					2 * (1 - t) * t * ctrlPoint.lng +
					Math.pow(t, 2) * drop.lng;

				latlngs.push(L.latLng(lat, lng));
			}

			// Create gradient colored route line
			routeLine = L.polyline(latlngs, {
				color: "#13E2DA",
				weight: 5,
				opacity: 0.8,
				lineJoin: "round",
				lineCap: "round",
				className: "animated-route",
			}).addTo(map);

			// Add animated dot along the path with enhanced animation
			animateMarker(latlngs, routeLine);

			// Fit map to show both markers with better padding
			var bounds = L.latLngBounds([pickup, drop]);
			map.fitBounds(bounds, { padding: [100, 100] });

			// Calculate distance in kilometers
			var distance = pickup.distanceTo(drop) / 1000;

			// Update both the visible text and hidden form field with animation
			if (distanceValueElement) {
				// Animate the distance counter
				animateCounter(parseFloat(distanceValueElement.textContent), distance);
				distanceValueElement.classList.add("pulse");
				setTimeout(() => {
					distanceValueElement.classList.remove("pulse");
				}, 1500);
			}

			if (distanceField) {
				distanceField.value = distance.toFixed(1);
			}

			// Add distance tooltip on the route
			var tooltip = L.marker(midPoint, {
				icon: L.divIcon({
					html: `<div class="distance-tooltip">${distance.toFixed(1)} km</div>`,
					className: "",
					iconSize: [80, 40],
				}),
			}).addTo(map);

			// Remove tooltip after 3 seconds
			setTimeout(() => {
				map.removeLayer(tooltip);
			}, 3000);

			// Add CSS for the tooltip
			document.head.insertAdjacentHTML(
				"beforeend",
				`
                <style>
                    .distance-tooltip {
                        background: white;
                        padding: 5px 10px;
                        border-radius: 20px;
                        font-weight: bold;
                        box-shadow: 0 5px 10px rgba(0,0,0,0.2);
                        color: #13E2DA;
                        border: 2px solid #13E2DA;
                        animation: tooltip-popup 0.3s ease-out;
                    }
                    
                    @keyframes tooltip-popup {
                        0% { transform: scale(0.5); opacity: 0; }
                        100% { transform: scale(1); opacity: 1; }
                    }
                </style>
            `
			);

			return distance;
		}
		return 0;
	}

	// Enhanced animation for distance counter
	function animateCounter(start, end) {
		var duration = 1200; // ms
		var startTime = null;
		var easing = (t) =>
			t < 0.5 ? 4 * t * t * t : (t - 1) * (2 * t - 2) * (2 * t - 2) + 1; // Cubic easing

		function step(timestamp) {
			if (!startTime) startTime = timestamp;
			var progress = Math.min((timestamp - startTime) / duration, 1);
			var easedProgress = easing(progress);
			var currentValue = start + easedProgress * (end - start);
			distanceValueElement.textContent = currentValue.toFixed(1);

			if (progress < 1) {
				window.requestAnimationFrame(step);
			}
		}

		window.requestAnimationFrame(step);
	}

	// Enhanced marker animation along the path
	function animateMarker(latlngs, line) {
		var animatedMarker = L.divIcon({
			html: '<div class="animated-dot"></div>',
			className: "",
			iconSize: [12, 12],
		});

		var marker = L.marker(latlngs[0], {
			icon: animatedMarker,
		}).addTo(map);

		var i = 0;
		var stepSize = 1;
		var delay = 50; // ms between steps

		function animateStep() {
			if (i >= latlngs.length) {
				map.removeLayer(marker);
				return;
			}

			marker.setLatLng(latlngs[i]);
			i += stepSize;

			// Speed up animation based on distance
			if (i > latlngs.length / 3) {
				stepSize = 2;
			}

			setTimeout(animateStep, delay);
		}

		animateStep();
	}

	// Enhanced "Use my location" button with better feedback
	var useMyLocationBtn = document.getElementById("useMyLocation");
	if (useMyLocationBtn) {
		useMyLocationBtn.addEventListener("click", function () {
			if (navigator.geolocation) {
				// Show loading indicator with pulsing animation
				useMyLocationBtn.innerHTML =
					'<i class="fas fa-spinner fa-spin"></i> Locating...';
				useMyLocationBtn.disabled = true;
				useMyLocationBtn.classList.add("pulsing-btn");

				// Add pulsing button style
				document.head.insertAdjacentHTML(
					"beforeend",
					`
                    <style>
                        .pulsing-btn {
                            animation: button-pulse 1.5s infinite;
                        }
                        
                        @keyframes button-pulse {
                            0% { box-shadow: 0 0 0 0 rgba(19,226,218,0.4); }
                            70% { box-shadow: 0 0 0 10px rgba(19,226,218,0); }
                            100% { box-shadow: 0 0 0 0 rgba(19,226,218,0); }
                        }
                    </style>
                `
				);

				navigator.geolocation.getCurrentPosition(
					function (position) {
						var lat = position.coords.latitude;
						var lng = position.coords.longitude;

						// Show initial marker with animation
						var pulsingIcon = L.divIcon({
							html: '<div class="pulsing-location"></div>',
							className: "",
							iconSize: [30, 30],
						});

						var tempMarker = L.marker([lat, lng], {
							icon: pulsingIcon,
						}).addTo(map);

						document.head.insertAdjacentHTML(
							"beforeend",
							`
                        <style>
                            .pulsing-location {
                                width: 30px;
                                height: 30px;
                                border-radius: 50%;
                                background: rgba(19,226,218,0.3);
                                box-shadow: 0 0 0 rgba(19,226,218, 0.4);
                                animation: pulsing-location 2s infinite;
                            }
                            
                            @keyframes pulsing-location {
                                0% { box-shadow: 0 0 0 0 rgba(19,226,218,0.4); transform: scale(0.9); opacity: 1; }
                                70% { box-shadow: 0 0 0 20px rgba(19,226,218,0); transform: scale(1.2); opacity: 0.7; }
                                100% { box-shadow: 0 0 0 0 rgba(19,226,218,0); transform: scale(0.9); opacity: 1; }
                            }
                        </style>
                    `
						);

						// Reverse geocode to get address
						fetch(
							`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}`
						)
							.then((response) => response.json())
							.then((data) => {
								var address = data.display_name;

								// Remove temp marker
								map.removeLayer(tempMarker);

								// Update input with success animation
								var pickupInput = document.getElementById("pickupLocation");
								pickupInput.value = address;
								pickupInput.classList.add("success-highlight");

								document.head.insertAdjacentHTML(
									"beforeend",
									`
                                <style>
                                    .success-highlight {
                                        background: linear-gradient(120deg, rgba(19,226,218,0.2) 0%, rgba(19,226,218,0.05) 100%) !important;
                                        transition: all 0.5s ease;
                                        border-color: #13E2DA !important;
                                    }
                                </style>
                            `
								);

								setTimeout(() => {
									pickupInput.classList.remove("success-highlight");
								}, 2000);

								// Update map
								if (pickupMarker) {
									map.removeLayer(pickupMarker);
								}

								pickupMarker = L.marker([lat, lng], {
									draggable: true,
									title: "Pickup Location",
									icon: pickupIcon,
								}).addTo(map);

								// Add enhanced popup with formatted address
								var popupContent = `
                                <div class="custom-popup">
                                    <div class="popup-header">
                                        <i class="fas fa-dot-circle"></i>
                                        <span>Pickup Location</span>
                                    </div>
                                    <div class="popup-address">${address}</div>
                                </div>
                            `;

								document.head.insertAdjacentHTML(
									"beforeend",
									`
                                <style>
                                    .custom-popup {
                                        padding: 5px;
                                    }
                                    
                                    .popup-header {
                                        display: flex;
                                        align-items: center;
                                        margin-bottom: 5px;
                                        font-weight: bold;
                                    }
                                    
                                    .popup-header i {
                                        background: linear-gradient(135deg, #13E2DA 0%, #5D3FD3 100%);
                                        -webkit-background-clip: text;
                                        -webkit-text-fill-color: transparent;
                                        margin-right: 5px;
                                    }
                                    
                                    .popup-address {
                                        font-size: 12px;
                                        color: #555;
                                        max-width: 200px;
                                    }
                                    
                                    .leaflet-popup-content-wrapper {
                                        border-radius: 8px;
                                        box-shadow: 0 5px 15px rgba(0,0,0,0.1);
                                    }
                                    
                                    .leaflet-popup-tip {
                                        box-shadow: 0 5px 15px rgba(0,0,0,0.1);
                                    }
                                </style>
                            `
								);

								pickupMarker.bindPopup(popupContent).openPopup();

								pickupMarker.on("dragend", function (e) {
									updatePickupFromMarker(e);
									calculateDistance();
								});

								// Center map with animation
								map.flyTo([lat, lng], 15, {
									animate: true,
									duration: 1.5,
								});

								calculateDistance();

								// Reset button with success animation
								useMyLocationBtn.innerHTML =
									'<i class="fas fa-check"></i> Location found';
								useMyLocationBtn.classList.remove("pulsing-btn");
								useMyLocationBtn.classList.add("success-btn");

								document.head.insertAdjacentHTML(
									"beforeend",
									`
                                <style>
                                    .success-btn {
                                        background: linear-gradient(135deg, #13E2DA 0%, #0bb1a8 100%) !important;
                                    }
                                </style>
                            `
								);

								setTimeout(() => {
									useMyLocationBtn.innerHTML =
										'<i class="fas fa-crosshairs"></i> Use my location';
									useMyLocationBtn.classList.remove("success-btn");
									useMyLocationBtn.disabled = false;
								}, 2000);
							})
							.catch((error) => {
								console.error("Error getting address: ", error);
								map.removeLayer(tempMarker);

								// Show error message
								alert(
									"Could not determine your address. Please type it manually."
								);
								useMyLocationBtn.innerHTML =
									'<i class="fas fa-crosshairs"></i> Use my location';
								useMyLocationBtn.classList.remove("pulsing-btn");
								useMyLocationBtn.disabled = false;
							});
					},
					function (error) {
						console.error("Geolocation error: ", error);

						// Show error with animation
						useMyLocationBtn.innerHTML =
							'<i class="fas fa-exclamation-triangle"></i> Error';
						useMyLocationBtn.classList.remove("pulsing-btn");
						useMyLocationBtn.classList.add("error-btn");

						document.head.insertAdjacentHTML(
							"beforeend",
							`
                        <style>
                            .error-btn {
                                background: linear-gradient(135deg, #FF6B6B 0%, #ee5b5b 100%) !important;
                            }
                        </style>
                    `
						);

						setTimeout(() => {
							useMyLocationBtn.innerHTML =
								'<i class="fas fa-crosshairs"></i> Use my location';
							useMyLocationBtn.classList.remove("error-btn");
							useMyLocationBtn.disabled = false;
						}, 2000);

						alert(
							"Could not get your location. Please allow location access or enter manually."
						);
					}
				);
			} else {
				alert("Geolocation is not supported by your browser.");
			}
		});
	}

	// Enhanced map controls with hover effects
	document.getElementById("zoomIn").addEventListener("click", function () {
		map.zoomIn();
		animateMapControl(this);
	});

	document.getElementById("zoomOut").addEventListener("click", function () {
		map.zoomOut();
		animateMapControl(this);
	});

	document.getElementById("centerMap").addEventListener("click", function () {
		if (pickupMarker && dropMarker) {
			var bounds = L.latLngBounds([
				pickupMarker.getLatLng(),
				dropMarker.getLatLng(),
			]);
			map.fitBounds(bounds, {
				padding: [80, 80],
				animate: true,
				duration: 1,
			});
		} else if (pickupMarker) {
			map.flyTo(pickupMarker.getLatLng(), 15, {
				animate: true,
				duration: 1,
			});
		} else if (dropMarker) {
			map.flyTo(dropMarker.getLatLng(), 15, {
				animate: true,
				duration: 1,
			});
		}

		animateMapControl(this);
	});

	// Function to animate map control buttons when clicked
	function animateMapControl(element) {
		element.classList.add("map-control-active");

		document.head.insertAdjacentHTML(
			"beforeend",
			`
            <style>
                .map-control-active {
                    transform: scale(0.9);
                    background: linear-gradient(135deg, #f0f0f0 0%, #e0e0e0 100%);
                }
            </style>
        `
		);

		setTimeout(() => {
			element.classList.remove("map-control-active");
		}, 200);
	}

	// Enhanced map click event with better visual feedback
	map.on("click", function (e) {
		// Determine which field to update based on focus
		var activeInput = document.activeElement;
		var isPickup = activeInput.id === "pickupLocation";
		var isDrop = activeInput.id === "dropLocation";

		if (!isPickup && !isDrop) {
			// If no field is focused, default to drop location if pickup exists
			isDrop = pickupMarker !== null;
			isPickup = !isDrop;
		}

		var lat = e.latlng.lat;
		var lng = e.latlng.lng;

		// Add enhanced loading effect
		var rippleEffect = L.divIcon({
			html: '<div class="map-click-ripple"></div>',
			className: "",
			iconSize: [80, 80],
			iconAnchor: [40, 40],
		});

		var rippleMarker = L.marker([lat, lng], {
			icon: rippleEffect,
		}).addTo(map);

		document.head.insertAdjacentHTML(
			"beforeend",
			`
            <style>
                .map-click-ripple {
                    width: 20px;
                    height: 20px;
                    background-color: ${isPickup ? "#13E2DA" : "#FF6B6B"};
                    border-radius: 50%;
                    position: relative;
                    opacity: 0.6;
                }
                
                .map-click-ripple:before {
                    content: '';
                    position: absolute;
                    top: 0;
                    left: 0;
                    right: 0;
                    bottom: 0;
                    border-radius: 50%;
                    animation: ripple-effect 1.5s ease-out infinite;
                    background-color: ${isPickup ? "#13E2DA" : "#FF6B6B"};
                }
                
                @keyframes ripple-effect {
                    0% { transform: scale(1); opacity: 0.7; }
                    100% { transform: scale(3); opacity: 0; }
                }
            </style>
        `
		);

		// Show loading text in the appropriate input
		var targetInput = isPickup
			? document.getElementById("pickupLocation")
			: document.getElementById("dropLocation");
		var originalValue = targetInput.value;

		targetInput.value = "Finding location...";
		targetInput.classList.add("loading-input");
		document.head.insertAdjacentHTML(
			"beforeend",
			`
            <style>
                .loading-input {
                    background-image: linear-gradient(to right, #f0f0f0 45%, #e0e0e0 50%, #f0f0f0 55%);
                    background-size: 200% 100%;
                    animation: loading-gradient 1.5s infinite linear;
                }
                
                @keyframes loading-gradient {
                    0% { background-position: 100% 50%; }
                    100% { background-position: 0% 50%; }
                }
            </style>
        `
		);

		// Perform reverse geocoding
		fetch(
			`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}`
		)
			.then((response) => response.json())
			.then((data) => {
				// Remove the ripple effect
				setTimeout(() => {
					map.removeLayer(rippleMarker);
				}, 800);

				var address = data.display_name;
				targetInput.value = address;
				targetInput.classList.remove("loading-input");
				targetInput.classList.add("highlight-input");

				setTimeout(() => {
					targetInput.classList.remove("highlight-input");
				}, 2000);

				// Update the appropriate marker
				if (isPickup) {
					if (pickupMarker) {
						map.removeLayer(pickupMarker);
					}

					pickupMarker = L.marker([lat, lng], {
						draggable: true,
						title: "Pickup Location",
						icon: pickupIcon,
					}).addTo(map);

					pickupMarker.on("dragend", function (e) {
						updatePickupFromMarker(e);
						calculateDistance();
					});
				} else {
					if (dropMarker) {
						map.removeLayer(dropMarker);
					}

					dropMarker = L.marker([lat, lng], {
						draggable: true,
						title: "Drop-off Location",
						icon: dropIcon,
					}).addTo(map);

					dropMarker.on("dragend", function (e) {
						updateDropFromMarker(e);
						calculateDistance();
					});
				}

				// Add enhanced popup with formatted address
				var popupContent = `
                    <div class="custom-popup">
                        <div class="popup-header">
                            <i class="fas fa-${
															isPickup ? "dot-circle" : "map-pin"
														}"></i>
                            <span>${
															isPickup ? "Pickup" : "Drop-off"
														} Location</span>
                        </div>
                        <div class="popup-address">${address}</div>
                    </div>
                `;

				var marker = isPickup ? pickupMarker : dropMarker;
				marker.bindPopup(popupContent).openPopup();

				calculateDistance();
			})
			.catch((error) => {
				console.error("Error getting address: ", error);
				targetInput.value = originalValue;
				targetInput.classList.remove("loading-input");

				// Remove the ripple effect
				map.removeLayer(rippleMarker);

				// Show error message
				alert(
					"Could not determine the address. Please try again or enter it manually."
				);
			});
	});

	// Function to update pickup location from marker drag
	function updatePickupFromMarker(e) {
		var lat = e.target.getLatLng().lat;
		var lng = e.target.getLatLng().lng;
		var pickupInput = document.getElementById("pickupLocation");

		// Show loading indicator
		pickupInput.value = "Updating location...";
		pickupInput.classList.add("loading-input");

		// Reverse geocode to get updated address
		fetch(
			`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}`
		)
			.then((response) => response.json())
			.then((data) => {
				var address = data.display_name;
				pickupInput.value = address;
				pickupInput.classList.remove("loading-input");
				pickupInput.classList.add("highlight-input");

				setTimeout(() => {
					pickupInput.classList.remove("highlight-input");
				}, 1500);

				// Update popup content
				var popupContent = `
                    <div class="custom-popup">
                        <div class="popup-header">
                            <i class="fas fa-dot-circle"></i>
                            <span>Pickup Location</span>
                        </div>
                        <div class="popup-address">${address}</div>
                    </div>
                `;

				e.target.setPopupContent(popupContent).openPopup();
			})
			.catch((error) => {
				console.error("Error updating address: ", error);
				pickupInput.value = "Location selected (Address unavailable)";
				pickupInput.classList.remove("loading-input");
			});
	}

	// Function to update drop location from marker drag
	function updateDropFromMarker(e) {
		var lat = e.target.getLatLng().lat;
		var lng = e.target.getLatLng().lng;
		var dropInput = document.getElementById("dropLocation");

		// Show loading indicator
		dropInput.value = "Updating location...";
		dropInput.classList.add("loading-input");

		// Reverse geocode to get updated address
		fetch(
			`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}`
		)
			.then((response) => response.json())
			.then((data) => {
				var address = data.display_name;
				dropInput.value = address;
				dropInput.classList.remove("loading-input");
				dropInput.classList.add("highlight-input");

				setTimeout(() => {
					dropInput.classList.remove("highlight-input");
				}, 1500);

				// Update popup content
				var popupContent = `
                    <div class="custom-popup">
                        <div class="popup-header">
                            <i class="fas fa-map-pin"></i>
                            <span>Drop-off Location</span>
                        </div>
                        <div class="popup-address">${address}</div>
                    </div>
                `;

				e.target.setPopupContent(popupContent).openPopup();
			})
			.catch((error) => {
				console.error("Error updating address: ", error);
				dropInput.value = "Location selected (Address unavailable)";
				dropInput.classList.remove("loading-input");
			});
	}

	// Enhanced input handling with geocoding search
	var pickupInput = document.getElementById("pickupLocation");
	var dropInput = document.getElementById("dropLocation");

	// Implement typeahead search with debounce for pickup location
	if (pickupInput) {
		attachLocationAutocomplete(pickupInput, function (location) {
			if (pickupMarker) {
				map.removeLayer(pickupMarker);
			}

			pickupMarker = L.marker([location.lat, location.lon], {
				draggable: true,
				icon: pickupIcon,
				title: "Pickup Location",
			}).addTo(map);

			pickupMarker.on("dragend", function (e) {
				updatePickupFromMarker(e);
				calculateDistance();
			});

			var popupContent = `
                <div class="custom-popup">
                    <div class="popup-header">
                        <i class="fas fa-dot-circle"></i>
                        <span>Pickup Location</span>
                    </div>
                    <div class="popup-address">${location.display_name}</div>
                </div>
            `;

			pickupMarker.bindPopup(popupContent).openPopup();

			// Center map with animation
			map.flyTo([location.lat, location.lon], 15, {
				animate: true,
				duration: 1,
			});

			calculateDistance();
		});
	}

	// Implement typeahead search with debounce for drop location
	if (dropInput) {
		attachLocationAutocomplete(dropInput, function (location) {
			if (dropMarker) {
				map.removeLayer(dropMarker);
			}

			dropMarker = L.marker([location.lat, location.lon], {
				draggable: true,
				icon: dropIcon,
				title: "Drop-off Location",
			}).addTo(map);

			dropMarker.on("dragend", function (e) {
				updateDropFromMarker(e);
				calculateDistance();
			});

			var popupContent = `
                <div class="custom-popup">
                    <div class="popup-header">
                        <i class="fas fa-map-pin"></i>
                        <span>Drop-off Location</span>
                    </div>
                    <div class="popup-address">${location.display_name}</div>
                </div>
            `;

			dropMarker.bindPopup(popupContent).openPopup();

			// Center map with animation
			map.flyTo([location.lat, location.lon], 15, {
				animate: true,
				duration: 1,
			});

			calculateDistance();
		});
	}

	// Function to handle location autocomplete
	function attachLocationAutocomplete(inputElement, onSelectCallback) {
		var dropdown = null;
		var debounceTimer = null;

		inputElement.addEventListener("input", function () {
			clearTimeout(debounceTimer);

			if (this.value.length < 3) {
				if (dropdown) {
					dropdown.remove();
					dropdown = null;
				}
				return;
			}

			debounceTimer = setTimeout(() => {
				const searchTerm = this.value;

				// Create or clear dropdown
				if (!dropdown) {
					dropdown = document.createElement("div");
					dropdown.className = "location-dropdown";
					inputElement.parentNode.appendChild(dropdown);
				} else {
					dropdown.innerHTML = "";
				}

				// Show loading indicator
				dropdown.innerHTML = `
                    <div class="location-loading">
                        <i class="fas fa-spinner"></i> Searching locations...
                    </div>
                `;

				// Fetch location suggestions
				fetch(
					`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(
						searchTerm
					)}`
				)
					.then((response) => response.json())
					.then((data) => {
						dropdown.innerHTML = "";

						if (data.length === 0) {
							dropdown.innerHTML = `
                                <div class="location-option no-results">
                                    No locations found
                                </div>
                            `;
							return;
						}

						// Limit to 5 results
						const results = data.slice(0, 5);

						results.forEach((location) => {
							const option = document.createElement("div");
							option.className = "location-option";
							option.innerHTML = location.display_name;

							option.addEventListener("click", function () {
								inputElement.value = location.display_name;

								// Animation for selection
								inputElement.classList.add("highlight-input");
								setTimeout(() => {
									inputElement.classList.remove("highlight-input");
								}, 1500);

								if (dropdown) {
									dropdown.remove();
									dropdown = null;
								}

								if (onSelectCallback) {
									onSelectCallback(location);
								}
							});

							dropdown.appendChild(option);
						});
					})
					.catch((error) => {
						console.error("Error searching locations:", error);
						dropdown.innerHTML = `
                            <div class="location-option error">
                                Error searching locations
                            </div>
                        `;
					});
			}, 500); // 500ms debounce
		});

		// Close dropdown when clicking outside
		document.addEventListener("click", function (e) {
			if (
				dropdown &&
				!inputElement.contains(e.target) &&
				!dropdown.contains(e.target)
			) {
				dropdown.remove();
				dropdown = null;
			}
		});

		// Handle keyboard navigation
		inputElement.addEventListener("keydown", function (e) {
			if (!dropdown) return;

			const options = dropdown.getElementsByClassName("location-option");
			let focusedIndex = Array.from(options).findIndex((opt) =>
				opt.classList.contains("focused")
			);

			switch (e.key) {
				case "ArrowDown":
					e.preventDefault();
					if (focusedIndex >= 0) {
						options[focusedIndex].classList.remove("focused");
					}
					focusedIndex = (focusedIndex + 1) % options.length;
					options[focusedIndex].classList.add("focused");
					options[focusedIndex].scrollIntoView({ block: "nearest" });
					break;

				case "ArrowUp":
					e.preventDefault();
					if (focusedIndex >= 0) {
						options[focusedIndex].classList.remove("focused");
					}
					focusedIndex = (focusedIndex - 1 + options.length) % options.length;
					options[focusedIndex].classList.add("focused");
					options[focusedIndex].scrollIntoView({ block: "nearest" });
					break;

				case "Enter":
					if (focusedIndex >= 0) {
						e.preventDefault();
						options[focusedIndex].click();
					}
					break;

				case "Escape":
					dropdown.remove();
					dropdown = null;
					break;
			}
		});

		// Add styles for dropdown keyboard navigation
		document.head.insertAdjacentHTML(
			"beforeend",
			`
            <style>
                .location-option.focused {
                    background-color: rgba(19,226,218,0.1);
                }
                
                .location-option.no-results,
                .location-option.error {
                    color: #666;
                    font-style: italic;
                    cursor: default;
                }
            </style>
        `
		);
	}

	// Add confetti animation for successful booking
	document
		.getElementById("bookingForm")
		.addEventListener("submit", function (e) {
			// If distance is too small or zero, show warning
			var distance = parseFloat(document.getElementById("distanceField").value);
			if (distance < 0.5) {
				e.preventDefault();

				if (
					confirm(
						"The selected locations are very close. Are you sure you want to create this booking?"
					)
				) {
					// Continue with form submission with manual resubmit
					this.submit();
				}
			} else {
				// Add a success message to the session to be shown after redirect
				sessionStorage.setItem("bookingSuccess", "true");
			}
		});

	// Check for success message on page load
	if (sessionStorage.getItem("bookingSuccess") === "true") {
		sessionStorage.removeItem("bookingSuccess");

		// Create confetti celebration
		createConfetti();
	}

	// Create confetti animation
	function createConfetti() {
		const confettiContainer = document.createElement("div");
		confettiContainer.className = "confetti-container";
		document.body.appendChild(confettiContainer);

		// Create confetti pieces with different colors
		const colors = ["#13E2DA", "#5D3FD3", "#FF6B6B", "#FFD700", "#06d6ce"];
		const totalConfetti = 150;

		for (let i = 0; i < totalConfetti; i++) {
			const confetti = document.createElement("div");
			confetti.className = "confetti-piece";
			confetti.style.backgroundColor =
				colors[Math.floor(Math.random() * colors.length)];
			confetti.style.left = Math.random() * 100 + "vw";
			confetti.style.animationDelay = Math.random() * 3 + "s";
			confetti.style.animationDuration = Math.random() * 3 + 2 + "s";
			confettiContainer.appendChild(confetti);
		}

		document.head.insertAdjacentHTML(
			"beforeend",
			`
            <style>
                .confetti-container {
                    position: fixed;
                    top: 0;
                    left: 0;
                    width: 100%;
                    height: 100%;
                    pointer-events: none;
                    z-index: 9999;
                }
                
                .confetti-piece {
                    position: absolute;
                    width: 10px;
                    height: 16px;
                    opacity: 0;
                    top: -20px;
                    transform-origin: center;
                    animation: confetti-fall linear forwards;
                }
                
                @keyframes confetti-fall {
                    0% {
                        opacity: 1;
                        top: -20px;
                        transform: rotate(0deg);
                    }
                    
                    50% {
                        opacity: 1;
                        transform: rotate(180deg);
                    }
                    
                    100% {
                        opacity: 0;
                        top: 100vh;
                        transform: rotate(360deg);
                    }
                }
            </style>
        `
		);

		// Remove confetti after animation completes
		setTimeout(() => {
			confettiContainer.remove();
		}, 5000);
	}

	// Initialize the map with a welcome animation
	function initMapWithAnimation() {
		// Add welcome message overlay
		const welcomeOverlay = document.createElement("div");
		welcomeOverlay.className = "map-welcome-overlay";
		welcomeOverlay.innerHTML = `
            <div class="welcome-content">
                <i class="fas fa-map-marked-alt welcome-icon"></i>
                <div class="welcome-text">
                    <h3>Interactive Booking Map</h3>
                    <p>Click on the map or search locations to set pickup and destination points</p>
                </div>
            </div>
        `;
		document.querySelector(".map-container").appendChild(welcomeOverlay);

		document.head.insertAdjacentHTML(
			"beforeend",
			`
            <style>
                .map-welcome-overlay {
                    position: absolute;
                    top: 0;
                    left: 0;
                    right: 0;
                    bottom: 0;
                    background: rgba(255,255,255,0.9);
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    z-index: 401;
                    animation: fade-welcome 3s forwards;
                    pointer-events: none;
                }
                
                .welcome-content {
                    text-align: center;
                    padding: 20px;
                    max-width: 80%;
                }
                
                .welcome-icon {
                    font-size: 40px;
                    margin-bottom: 15px;
                    background: linear-gradient(135deg, #13E2DA 0%, #5D3FD3 100%);
                    -webkit-background-clip: text;
                    -webkit-text-fill-color: transparent;
                    animation: welcome-icon-bounce 1s ease-out;
                }
                
                .welcome-text h3 {
                    color: #333;
                    margin-bottom: 10px;
                    animation: welcome-text-slide 1s ease-out;
                }
                
                .welcome-text p {
                    color: #666;
                    animation: welcome-text-slide 1.2s ease-out;
                }
                
                @keyframes fade-welcome {
                    0% { opacity: 1; }
                    70% { opacity: 1; }
                    100% { opacity: 0; }
                }
                
                @keyframes welcome-icon-bounce {
                    0% { transform: scale(0.5); opacity: 0; }
                    50% { transform: scale(1.2); }
                    100% { transform: scale(1); opacity: 1; }
                }
                
                @keyframes welcome-text-slide {
                    0% { transform: translateY(20px); opacity: 0; }
                    100% { transform: translateY(0); opacity: 1; }
                }
            </style>
        `
		);

		// Remove overlay after animation
		setTimeout(() => {
			welcomeOverlay.remove();
		}, 3000);
	}

	// Call the welcome animation
	initMapWithAnimation();
});