// This file should be saved as vehicleSelector.js
document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('vehicleSelectionModal');
    const openModalBtn = document.getElementById('openVehicleModal');
    const closeBtn = document.querySelector('.close');
    const confirmBtn = document.getElementById('confirmVehicle');
    const vehicleCards = document.querySelectorAll('.vehicle-card');
    const vehicleIdInput = document.getElementById('selectedVehicleId');
    const bookingForm = document.getElementById('bookingForm');
    const distanceField = document.getElementById('distanceField'); // Added this line

    // Open the modal with animation
    openModalBtn.addEventListener('click', function() {
        // Validate that locations are filled
        const pickup = document.getElementById('pickupLocation').value;
        const dropoff = document.getElementById('dropLocation').value;

        if (!pickup || !dropoff) {
            alert('Please fill in both pickup and drop-off locations.');
            return;
        }

        // Show modal
        modal.style.display = 'block';
        // Force reflow (for animation to work)
        modal.offsetWidth;
        // Add active class for animation
        modal.classList.add('active');

        // Initially disable confirm button until selection is made
        confirmBtn.disabled = true;
    });

    // Close the modal with animation
    function closeModal() {
        modal.classList.remove('active');
        setTimeout(() => {
            modal.style.display = 'none';
        }, 300); // Match the transition duration
    }

    closeBtn.addEventListener('click', closeModal);

    // Close modal when clicking outside of it
    window.addEventListener('click', function(event) {
        if (event.target === modal) {
            closeModal();
        }
    });

    // Handle vehicle selection
    vehicleCards.forEach(function(card) {
        card.addEventListener('click', function() {
            // Remove selected class from all cards
            vehicleCards.forEach(c => c.classList.remove('selected'));
            // Add selected class to clicked card
            this.classList.add('selected');
            // Get the vehicle ID
            const vehicleId = this.getAttribute('data-vehicle-id');
            // Update hidden input
            vehicleIdInput.value = vehicleId;
            // Enable confirm button
            confirmBtn.disabled = false;
        });
    });

    // Handle confirm button
    confirmBtn.addEventListener('click', function() {
        if (vehicleIdInput.value) {
            // Get the distance value
            distanceField.value = document.getElementById('distanceValue').textContent;

            closeModal();

            // Submit the form after modal closes
            setTimeout(() => {
                bookingForm.submit();
            }, 300);
        } else {
            alert('Please select a vehicle first.');
        }
    });
});