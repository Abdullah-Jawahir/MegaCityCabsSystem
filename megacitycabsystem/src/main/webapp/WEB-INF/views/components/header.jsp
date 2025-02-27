<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="top-header">
    <div class="header-search">
        <i class="fas fa-search"></i>
        <input type="text" id="global-search" placeholder="Search in this page..." onkeyup="performTableSearch(this.value)">
        <span id="search-count" class="search-count"></span>
    </div>
    <div class="header-right">
        <div class="notifications">
            <i class="fas fa-bell"></i>
            <span class="badge">3</span>
        </div>
        <div class="admin-profile">
            <img src="<c:url value='/assets/images/user-profile.avif'/>" alt="Admin">
            <span class="admin-name">
                <c:out value="${sessionScope.user.name}" default="ADMIN"/>
            </span>
        </div>
    </div>
</header>

<script>
    // This search functionality is completely client-side.
    // No server-provided searchText is used.

    // Store the timeout ID for debouncing
    let searchTimeout;

    function performTableSearch(searchText) {
        // Debounce the search to improve performance
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => {
            executeSearch(searchText);
        }, 300);
    }

    function executeSearch(searchText) {
        // Convert search text to lowercase for case-insensitive search
        searchText = searchText.toLowerCase().trim();

        // Find the data table on the current page
        const dataTable = document.querySelector('.data-table');
        const searchCountElement = document.getElementById('search-count');

        // If no table is found, do nothing
        if (!dataTable) {
            if (searchCountElement) {
                searchCountElement.textContent = '';
            }
            return;
        }

        // Get all table rows (skip the header row)
        const rows = dataTable.querySelectorAll('tbody tr');

        // Counter for visible rows
        let visibleRows = 0;

        // If search is cleared, show all rows and remove any highlights
        if (searchText === '') {
            rows.forEach(row => {
                row.style.display = '';

                // Remove highlights and restore original text if stored
                const cells = row.querySelectorAll('td');
                cells.forEach(cell => {
                    if (cell.hasAttribute('data-original-text')) {
                        cell.innerHTML = cell.getAttribute('data-original-text');
                    }
                });
            });

            if (searchCountElement) {
                searchCountElement.textContent = '';
            }

            const noResultsMessage = document.getElementById('no-results-message');
            if (noResultsMessage) {
                noResultsMessage.style.display = 'none';
            }
            return;
        }

        // Loop through rows to check for matching search text
        rows.forEach(row => {
            const cells = row.querySelectorAll('td');
            let rowContainsSearchText = false;

            cells.forEach(cell => {
                if (cell.textContent.toLowerCase().includes(searchText)) {
                    rowContainsSearchText = true;
                }
            });

            if (rowContainsSearchText) {
                row.style.display = '';
                visibleRows++;

                // Highlight matching text
                cells.forEach(cell => {
                    // Save original text if not already stored
                    if (!cell.hasAttribute('data-original-text')) {
                        cell.setAttribute('data-original-text', cell.textContent); // Use textContent instead of innerHTML
                    }

                    const originalText = cell.getAttribute('data-original-text');
                    
                    // Escape search text for regex usage
                    const escapedSearchText = searchText.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
                    const regex = new RegExp('(' + escapedSearchText + ')', 'gi');

                    // Create a temporary element to avoid altering existing HTML tags
                    const tempElement = document.createElement('span');
                    tempElement.textContent = originalText; // Preserve original structure

                    // Replace only text content, keeping HTML structure intact
                    tempElement.innerHTML = tempElement.textContent.replace(regex, '<span class="highlight">$1</span>');

                    // Assign only the modified text back while keeping the structure intact
                    cell.innerHTML = tempElement.innerHTML;
                });
            } else {
                row.style.display = 'none';
            }
        });

        // Update search count display using pure JavaScript string concatenation
        if (searchCountElement) {
            if (visibleRows > 0) {
                searchCountElement.textContent = visibleRows + " result" + (visibleRows !== 1 ? "s" : "") + " found";
            } else {
                searchCountElement.textContent = "No results found";
            }
        }

        // Display a "no results" message if no matching rows are found
        const noResultsMessage = document.getElementById('no-results-message');
        if (visibleRows === 0) {
            if (!noResultsMessage) {
                const message = document.createElement('div');
                message.id = 'no-results-message';
                message.className = 'no-results-message';
                message.innerHTML = 'No matching records found for <strong>"' + searchText + '"</strong>';
                dataTable.parentNode.insertBefore(message, dataTable.nextSibling);
            } else {
                noResultsMessage.style.display = 'block';
                noResultsMessage.innerHTML = 'No matching records found for <strong>"' + searchText + '"</strong>';
            }
        } else if (noResultsMessage) {
            noResultsMessage.style.display = 'none';
        }
    }

    // Keyboard shortcuts: Ctrl+F to focus search input, ESC to clear it
    document.addEventListener('keydown', function(e) {
        if ((e.ctrlKey || e.metaKey) && e.key === 'f') {
            e.preventDefault();
            document.getElementById('global-search').focus();
        }
        if (e.key === 'Escape') {
            const searchInput = document.getElementById('global-search');
            if (document.activeElement === searchInput) {
                searchInput.value = '';
                performTableSearch('');
                searchInput.blur();
            }
        }
    });

    // If the URL contains a search parameter, initialize the search input accordingly
    document.addEventListener('DOMContentLoaded', function() {
        const urlParams = new URLSearchParams(window.location.search);
        const searchParam = urlParams.get('search');
        if (searchParam) {
            const searchInput = document.getElementById('global-search');
            searchInput.value = searchParam;
            performTableSearch(searchParam);
            searchInput.focus();
        }
    });
</script>
