// Fix for distance calculation not updating
document.addEventListener('DOMContentLoaded', function() {
    // Reference to map and distance elements
    var distanceValueElement = document.getElementById('distanceValue');
    var distanceField = document.getElementById('distanceField');
    
    // Check if map exists (prevents errors if map element isn't loaded yet)
    if (!document.getElementById('map')) {
        console.error("Map element not found");
        return;
    }
    
    // Initialize map using Leaflet.js
    var map = L.map('map').setView([40.7128, -74.0060], 13);
    
    // Add OpenStreetMap tiles
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);
    
    // Markers for pickup and drop locations
    var pickupMarker = null;
    var dropMarker = null;
    var routeLine = null;
    
    // FIXED: Calculate and display the distance between markers
    function calculateDistance() {
        console.log("Calculating distance...");
        if (pickupMarker && dropMarker) {
            var pickup = pickupMarker.getLatLng();
            var drop = dropMarker.getLatLng();
            
            // Draw route line
            if (routeLine) {
                map.removeLayer(routeLine);
            }
            
            routeLine = L.polyline([pickup, drop], {
                color: '#13E2DA',
                weight: 5,
                opacity: 0.7,
                dashArray: '10, 10'
            }).addTo(map);
            
            // Fit map to show both markers
            var bounds = L.latLngBounds([pickup, drop]);
            map.fitBounds(bounds, { padding: [50, 50] });
            
            // Calculate distance in kilometers
            var distance = pickup.distanceTo(drop) / 1000;
            
            // IMPORTANT: Update both the visible text and hidden form field
            if (distanceValueElement) {
                distanceValueElement.textContent = distance.toFixed(1);
            }
            
            if (distanceField) {
                distanceField.value = distance.toFixed(1);
            }
            
            return distance;
        }
        return 0;
    }
    
    // Handle "Use my location" button
    var useMyLocationBtn = document.getElementById('useMyLocation');
    if (useMyLocationBtn) {
        useMyLocationBtn.addEventListener('click', function() {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(function(position) {
                    var lat = position.coords.latitude;
                    var lng = position.coords.longitude;
                    
                    // Reverse geocode to get address
                    fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}`)
                        .then(response => response.json())
                        .then(data => {
                            var address = data.display_name;
                            document.getElementById('pickupLocation').value = address;
                            
                            // Update map
                            if (pickupMarker) {
                                map.removeLayer(pickupMarker);
                            }
                            
                            pickupMarker = L.marker([lat, lng], {
                                draggable: true,
                                title: "Pickup Location"
                            }).addTo(map);
                            
                            pickupMarker.on('dragend', function(e) {
                                updatePickupFromMarker(e);
                                // FIXED: Explicitly call calculateDistance after marker update
                                calculateDistance();
                            });
                            
                            // Center map on user location
                            map.setView([lat, lng], 15);
                            
                            // FIXED: Explicitly call calculateDistance
                            calculateDistance();
                        })
                        .catch(error => {
                            console.error("Error getting address: ", error);
                            alert("Could not determine your address. Please type it manually.");
                        });
                }, function(error) {
                    console.error("Geolocation error: ", error);
                    alert("Could not get your location. Please allow location access or enter manually.");
                });
            } else {
                alert("Geolocation is not supported by your browser.");
            }
        });
    }
    
    // Setup map click event for selecting locations
    map.on('click', function(e) {
        // Determine which field to update based on focus
        var activeInput = document.activeElement;
        var isPickup = activeInput.id === 'pickupLocation';
        var isDrop = activeInput.id === 'dropLocation';
        
        if (!isPickup && !isDrop) {
            // If no field is focused, default to drop location if pickup exists
            isDrop = pickupMarker !== null;
            isPickup = !isDrop;
        }
        
        var lat = e.latlng.lat;
        var lng = e.latlng.lng;
        
        // Reverse geocode to get address
        fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}`)
            .then(response => response.json())
            .then(data => {
                var address = data.display_name;
                
                if (isPickup) {
                    document.getElementById('pickupLocation').value = address;
                    
                    if (pickupMarker) {
                        map.removeLayer(pickupMarker);
                    }
                    
                    pickupMarker = L.marker([lat, lng], {
                        draggable: true,
                        title: "Pickup Location"
                    }).addTo(map);
                    
                    pickupMarker.on('dragend', function(e) {
                        updatePickupFromMarker(e);
                        // FIXED: Explicitly call calculateDistance after marker update
                        calculateDistance();
                    });
                } else {
                    document.getElementById('dropLocation').value = address;
                    
                    if (dropMarker) {
                        map.removeLayer(dropMarker);
                    }
                    
                    dropMarker = L.marker([lat, lng], {
                        draggable: true,
                        title: "Drop Location",
                        icon: L.icon({
                            iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
                            shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
                            iconSize: [25, 41],
                            iconAnchor: [12, 41],
                            popupAnchor: [1, -34],
                            shadowSize: [41, 41]
                        })
                    }).addTo(map);
                    
                    dropMarker.on('dragend', function(e) {
                        updateDropFromMarker(e);
                        // FIXED: Explicitly call calculateDistance after marker update
                        calculateDistance();
                    });
                }
                
                // FIXED: Add console log to track calculation
                console.log("Both markers exist?", !!pickupMarker && !!dropMarker);
                
                // Calculate distance if both markers exist
                calculateDistance();
            })
            .catch(error => {
                console.error("Error getting address: ", error);
            });
    });
    
    // Update pickup location when marker is dragged
    function updatePickupFromMarker(e) {
        var marker = e.target;
        var position = marker.getLatLng();
        
        fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${position.lat}&lon=${position.lng}`)
            .then(response => response.json())
            .then(data => {
                document.getElementById('pickupLocation').value = data.display_name;
                // FIXED: Make sure to call calculateDistance here
                calculateDistance();
            });
    }
    
    // Update drop location when marker is dragged
    function updateDropFromMarker(e) {
        var marker = e.target;
        var position = marker.getLatLng();
        
        fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${position.lat}&lon=${position.lng}`)
            .then(response => response.json())
            .then(data => {
                document.getElementById('dropLocation').value = data.display_name;
                // FIXED: Make sure to call calculateDistance here
                calculateDistance();
            });
    }
    
    // Handle location inputs for address search
    var pickupInput = document.getElementById('pickupLocation');
    var dropInput = document.getElementById('dropLocation');
    
    // Search for location when user types and stops
    if (pickupInput) setupAddressSearch(pickupInput, true);
    if (dropInput) setupAddressSearch(dropInput, false);
    
    function setupAddressSearch(input, isPickup) {
        var timeout = null;
        
        input.addEventListener('input', function() {
            clearTimeout(timeout);
            
            timeout = setTimeout(function() {
                var query = input.value;
                if (query.length > 3) {
                    searchAddress(query, isPickup);
                }
            }, 500);
        });
    }
    
    function searchAddress(query, isPickup) {
        fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(query)}`)
            .then(response => response.json())
            .then(data => {
                if (data.length > 0) {
                    var result = data[0];
                    var lat = parseFloat(result.lat);
                    var lng = parseFloat(result.lon);
                    
                    if (isPickup) {
                        if (pickupMarker) {
                            map.removeLayer(pickupMarker);
                        }
                        
                        pickupMarker = L.marker([lat, lng], {
                            draggable: true,
                            title: "Pickup Location"
                        }).addTo(map);
                        
                        pickupMarker.on('dragend', function(e) {
                            updatePickupFromMarker(e);
                            calculateDistance();
                        });
                    } else {
                        if (dropMarker) {
                            map.removeLayer(dropMarker);
                        }
                        
                        dropMarker = L.marker([lat, lng], {
                            draggable: true,
                            title: "Drop Location",
                            icon: L.icon({
                                iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
                                shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
                                iconSize: [25, 41],
                                iconAnchor: [12, 41],
                                popupAnchor: [1, -34],
                                shadowSize: [41, 41]
                            })
                        }).addTo(map);
                        
                        dropMarker.on('dragend', function(e) {
                            updateDropFromMarker(e);
                            calculateDistance();
                        });
                    }
                    
                    map.setView([lat, lng], 15);
                    
                    // FIXED: Explicit delay before calculating distance
                    setTimeout(calculateDistance, 100);
                }
            })
            .catch(error => {
                console.error("Error searching address: ", error);
            });
    }
    
    // Form validation before submission
    var bookingForm = document.getElementById('bookingForm');
    if (bookingForm) {
        bookingForm.addEventListener('submit', function(e) {
            if (!pickupMarker || !dropMarker) {
                e.preventDefault();
                alert('Please select both pickup and drop locations on the map');
            } else {
                // FIXED: Force distance calculation one more time before submission
                var distance = calculateDistance();
                console.log("Form submission with distance: " + distance + " km");
            }
        });
    }
    
    // FIXED: Add MutationObserver to detect when map container becomes visible
    // (helpful if map is initially hidden, e.g., in a tab)
    var mapContainer = document.querySelector('.map-container');
    if (mapContainer) {
        var observer = new MutationObserver(function(mutations) {
            mutations.forEach(function(mutation) {
                if (mutation.attributeName === 'style' || 
                    mutation.attributeName === 'class') {
                    // Invalidate map size when container visibility changes
                    map.invalidateSize();
                    
                    // Recalculate distance if markers exist
                    if (pickupMarker && dropMarker) {
                        calculateDistance();
                    }
                }
            });
        });
        
        observer.observe(mapContainer, { attributes: true });
    }
    
    // FIXED: Force map to resize after a short delay
    // This helps when the map is initially loaded in a hidden container
    setTimeout(function() {
        map.invalidateSize();
    }, 500);
});