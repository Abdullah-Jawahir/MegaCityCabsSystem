document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('vehicleSelectionModal');
    const openModalBtn = document.getElementById('openVehicleModal');
    const closeBtn = document.querySelector('.close');
    const confirmBtn = document.getElementById('confirmVehicle');
    const vehicleCards = document.querySelectorAll('.vehicle-card');
    const vehicleIdInput = document.getElementById('selectedVehicleId');
    const bookingForm = document.getElementById('bookingForm');
    const distanceField = document.getElementById('distanceField');

    // Open the modal with animation
    openModalBtn.addEventListener('click', function() {
        // Validate that locations are filled
        const pickup = document.getElementById('pickupLocation').value;
        const dropoff = document.getElementById('dropLocation').value;

        if (!pickup || !dropoff) {
            alert('Please fill in both pickup and drop-off locations.');
            return;
        }


        modal.style.display = 'block';
        // Force reflow (for animation to work)
        modal.offsetWidth;

        modal.classList.add('active');


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

            vehicleCards.forEach(c => c.classList.remove('selected'));

            this.classList.add('selected');

            const vehicleId = this.getAttribute('data-vehicle-id');

            vehicleIdInput.value = vehicleId;

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