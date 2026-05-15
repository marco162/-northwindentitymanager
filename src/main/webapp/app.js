(function () {
    const apiUrl = "api/categories";
    const form = document.getElementById("category-form");
    const idField = document.getElementById("category-id");
    const nameField = document.getElementById("category-name");
    const descriptionField = document.getElementById("category-description");
    const submitButton = document.getElementById("submit-btn");
    const cancelEditButton = document.getElementById("cancel-edit-btn");
    const resetButton = document.getElementById("reset-btn");
    const refreshButton = document.getElementById("refresh-btn");
    const searchInput = document.getElementById("search-input");
    const tableBody = document.getElementById("category-table-body");
    const statusBanner = document.getElementById("status-banner");
    const counter = document.getElementById("counter");
    const formTitle = document.getElementById("form-title");

    let categories = [];
    let filteredCategories = [];

    function request(method, url, payload, onSuccess, onError) {
        const xhr = new XMLHttpRequest();
        xhr.open(method, url, true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

        xhr.onreadystatechange = function () {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }

            if (xhr.status >= 200 && xhr.status < 300) {
                const data = xhr.responseText ? JSON.parse(xhr.responseText) : null;
                onSuccess(data);
                return;
            }

            let message = "Operazione non completata.";
            if (xhr.responseText) {
                try {
                    const errorData = JSON.parse(xhr.responseText);
                    if (errorData.message) {
                        message = errorData.message;
                    }
                } catch (error) {
                    message = xhr.responseText;
                }
            }
            onError(message);
        };

        xhr.onerror = function () {
            onError("Connessione al server non riuscita.");
        };

        xhr.send(payload ? JSON.stringify(payload) : null);
    }

    function loadCategories(showSuccessMessage) {
        request("GET", apiUrl, null, function (data) {
            categories = Array.isArray(data) ? data : [];
            applyFilter();
            if (showSuccessMessage) {
                showStatus("Elenco aggiornato con successo.", "success");
            }
        }, function (message) {
            showStatus(message, "error");
        });
    }

    function applyFilter() {
        const term = searchInput.value.trim().toLowerCase();
        filteredCategories = categories.filter(function (category) {
            const name = (category.categoryName || "").toLowerCase();
            const description = (category.description || "").toLowerCase();
            return name.indexOf(term) !== -1 || description.indexOf(term) !== -1;
        });
        renderTable();
    }

    function renderTable() {
        if (!filteredCategories.length) {
            tableBody.innerHTML = '<tr><td colspan="4" class="empty-state">Nessuna categoria trovata.</td></tr>';
            counter.textContent = categories.length + " categorie nel database";
            return;
        }

        const rows = filteredCategories.map(function (category) {
            return [
                "<tr>",
                "<td>" + escapeHtml(String(category.categoryId)) + "</td>",
                "<td><strong>" + escapeHtml(category.categoryName || "") + "</strong></td>",
                "<td>" + escapeHtml(category.description || "Nessuna descrizione") + "</td>",
                "<td>",
                '<div class="row-actions">',
                '<button class="action-button edit-button" type="button" data-action="edit" data-id="' + category.categoryId + '">Modifica</button>',
                '<button class="action-button delete-button" type="button" data-action="delete" data-id="' + category.categoryId + '">Elimina</button>',
                "</div>",
                "</td>",
                "</tr>"
            ].join("");
        }).join("");

        tableBody.innerHTML = rows;
        counter.textContent = filteredCategories.length + " risultati visibili su " + categories.length + " categorie";
    }

    function escapeHtml(value) {
        return value
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#39;");
    }

    function showStatus(message, type) {
        statusBanner.textContent = message;
        statusBanner.className = "status-banner " + type;
    }

    function clearStatus() {
        statusBanner.textContent = "";
        statusBanner.className = "status-banner hidden";
    }

    function resetFormState() {
        idField.value = "";
        form.reset();
        formTitle.textContent = "Nuova categoria";
        submitButton.textContent = "Salva categoria";
        cancelEditButton.classList.add("hidden");
    }

    function startEdit(categoryId) {
        const category = categories.find(function (item) {
            return String(item.categoryId) === String(categoryId);
        });

        if (!category) {
            showStatus("Categoria non trovata.", "error");
            return;
        }

        idField.value = category.categoryId;
        nameField.value = category.categoryName || "";
        descriptionField.value = category.description || "";
        formTitle.textContent = "Modifica categoria #" + category.categoryId;
        submitButton.textContent = "Aggiorna categoria";
        cancelEditButton.classList.remove("hidden");
        clearStatus();
        window.scrollTo({ top: 0, behavior: "smooth" });
    }

    function deleteCategory(categoryId) {
        const category = categories.find(function (item) {
            return String(item.categoryId) === String(categoryId);
        });
        const label = category ? category.categoryName : "questa categoria";
        if (!window.confirm('Confermi l\'eliminazione di "' + label + '"?')) {
            return;
        }

        request("DELETE", apiUrl + "?id=" + encodeURIComponent(categoryId), null, function (data) {
            if (String(idField.value) === String(categoryId)) {
                resetFormState();
            }
            loadCategories(false);
            showStatus(data.message || "Categoria eliminata.", "success");
        }, function (message) {
            showStatus(message, "error");
        });
    }

    form.addEventListener("submit", function (event) {
        event.preventDefault();
        clearStatus();

        const payload = {
            categoryName: nameField.value.trim(),
            description: descriptionField.value.trim()
        };

        const editingId = idField.value.trim();
        const method = editingId ? "PUT" : "POST";
        const url = editingId ? apiUrl + "?id=" + encodeURIComponent(editingId) : apiUrl;

        request(method, url, payload, function (data) {
            resetFormState();
            loadCategories(false);
            showStatus(
                editingId
                    ? 'Categoria "' + data.categoryName + '" aggiornata con successo.'
                    : 'Categoria "' + data.categoryName + '" creata con successo.',
                "success"
            );
        }, function (message) {
            showStatus(message, "error");
        });
    });

    resetButton.addEventListener("click", function () {
        resetFormState();
        clearStatus();
    });

    cancelEditButton.addEventListener("click", function () {
        resetFormState();
        showStatus("Modifica annullata.", "success");
    });

    refreshButton.addEventListener("click", function () {
        clearStatus();
        loadCategories(true);
    });

    searchInput.addEventListener("input", applyFilter);

    tableBody.addEventListener("click", function (event) {
        const button = event.target.closest("button[data-action]");
        if (!button) {
            return;
        }

        const action = button.getAttribute("data-action");
        const categoryId = button.getAttribute("data-id");

        if (action === "edit") {
            startEdit(categoryId);
        }

        if (action === "delete") {
            deleteCategory(categoryId);
        }
    });

    resetFormState();
    loadCategories(false);
})();
