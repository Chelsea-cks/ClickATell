
"use strict";

// 1. Get the website elements


const roleSelect = document.getElementById("roleSelect");
const visitorSection = document.getElementById("visitorSection");
const organiserSection = document.getElementById("organiserSection");
const adminSection = document.getElementById("adminSection");
const messageBox = document.getElementById("message");

const eventList = document.getElementById("eventList");
const eventDetails = document.getElementById("eventDetails");
const detailsTitle = document.getElementById("detailsTitle");
const detailsContent = document.getElementById("detailsContent");
const registerButton = document.getElementById("registerButton");

const createEventForm = document.getElementById("createEventForm");
const organiserIdInput = document.getElementById("organiserId");
const myEventsList = document.getElementById("myEventsList");
const adminEventsList = document.getElementById("adminEventsList");


// 2. Store the selected event


let selectedEvent = null;


// 3. Show messages to the user


function showMessage(message, type = "success") {
    messageBox.textContent = message;
    messageBox.className = `message ${type}`;
    messageBox.hidden = false;

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}

function clearMessage() {
    messageBox.textContent = "";
    messageBox.hidden = true;
}


// 4. Communicate with the Spring Boot API


async function apiRequest(url, options = {}) {
    const response = await fetch(url, options);

    const responseText = await response.text();
    let data = null;

    if (responseText) {
        try {
            data = JSON.parse(responseText);
        } catch {
            data = responseText;
        }
    }

    if (!response.ok) {
        let errorMessage = "The request could not be completed.";

        if (data && typeof data === "object") {
            errorMessage =
                data.message ||
                data.error ||
                data.detail ||
                errorMessage;
        } else if (typeof data === "string" && data.trim()) {
            errorMessage = data;
        }

        throw new Error(errorMessage);
    }

    return data;
}

function jsonOptions(method, body) {
    return {
        method: method,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    };
}

// 5. Safely create HTML elements

function createElement(tag, className, text) {
    const element = document.createElement(tag);

    if (className) {
        element.className = className;
    }

    if (text !== undefined && text !== null) {
        element.textContent = String(text);
    }

    return element;
}

function formatDate(dateTime) {
    if (!dateTime) {
        return "Date not available";
    }

    const date = new Date(dateTime);

    if (Number.isNaN(date.getTime())) {
        return String(dateTime);
    }

    return date.toLocaleString();
}

function addTextLine(container, label, value) {
    const paragraph = document.createElement("p");
    const strong = document.createElement("strong");

    strong.textContent = `${label}: `;
    paragraph.appendChild(strong);
    paragraph.appendChild(
        document.createTextNode(
            value === null || value === undefined
                ? "Not available"
                : String(value)
        )
    );

    container.appendChild(paragraph);
}

function addStatus(container, status) {
    const badge = createElement(
        "span",
        `status status-${String(status || "").toLowerCase()}`,
        status || "UNKNOWN"
    );

    container.appendChild(badge);
}


// 6. Display event cards


function createEventCard(event, showDetailsButton = false) {
    const card = createElement("article", "event-card");

    card.appendChild(createElement("h3", "", event.title));

    card.appendChild(
        createElement(
            "p",
            "",
            event.description || "No description provided."
        )
    );

    addTextLine(card, "Date", formatDate(event.dateTime));
    addTextLine(card, "Capacity", event.capacity);

    const statusContainer = document.createElement("p");
    addStatus(statusContainer, event.status);
    card.appendChild(statusContainer);

    if (showDetailsButton) {
        const button = createElement(
            "button",
            "",
            "View details"
        );

        button.type = "button";

        button.addEventListener("click", function () {
            showEventDetails(event);
        });

        card.appendChild(button);
    }

    return card;
}

function showEmptyState(container, message) {
    container.replaceChildren(
        createElement("p", "empty-state", message)
    );
}

// 7. Visitor: load published events


async function loadPublishedEvents() {
    eventList.replaceChildren(
        createElement("p", "", "Loading events...")
    );

    eventDetails.hidden = true;
    selectedEvent = null;

    try {
        const events = await apiRequest("/api/events");

        eventList.replaceChildren();

        if (!Array.isArray(events) || events.length === 0) {
            showEmptyState(
                eventList,
                "There are no published events available yet."
            );
            return;
        }

        events.forEach(function (event) {
            eventList.appendChild(createEventCard(event, true));
        });
    } catch (error) {
        showEmptyState(
            eventList,
            "Could not load events. Please check that the application is running."
        );

        showMessage(error.message, "error");
    }
}

// 8. Visitor: view event details


function showEventDetails(event) {
    selectedEvent = event;

    detailsTitle.textContent = event.title;
    detailsContent.replaceChildren();

    addTextLine(detailsContent, "Event ID", event.id);
    addTextLine(detailsContent, "Description", event.description);
    addTextLine(detailsContent, "Date and time", formatDate(event.dateTime));
    addTextLine(detailsContent, "Capacity", event.capacity);
    addTextLine(detailsContent, "Status", event.status);

    eventDetails.hidden = false;

    registerButton.disabled = false;
    registerButton.textContent = "Register for event";

    eventDetails.scrollIntoView({
        behavior: "smooth",
        block: "start"
    });
}


// 9. Visitor: register for an event


registerButton.addEventListener("click", async function () {
    if (!selectedEvent) {
        showMessage("Please select an event first.", "error");
        return;
    }

    registerButton.disabled = true;
    registerButton.textContent = "Registering...";

    try {
        const result = await apiRequest(
            `/api/events/${selectedEvent.id}/registrations`,
            { method: "POST" }
        );

        const registrationId =
            result && result.id !== undefined
                ? result.id
                : null;

        if (registrationId !== null) {
            showMessage(
                `Registration successful! Your registration ID is ${registrationId}.`
            );
        } else {
            showMessage(
                "Registration successful! Your registration has been recorded."
            );
        }

        registerButton.textContent = "Registration submitted";
    } catch (error) {
        showMessage(error.message, "error");
        registerButton.disabled = false;
        registerButton.textContent = "Register for event";
    }
});


// 10. Organiser: create an event


createEventForm.addEventListener("submit", async function (event) {
    event.preventDefault();
    clearMessage();

    const organiserId = organiserIdInput.value.trim();
    const title = document.getElementById("eventTitle").value.trim();
    const description = document.getElementById("eventDescription").value.trim();
    const dateTimeInput = document.getElementById("eventDateTime").value;
    const capacity = Number(document.getElementById("eventCapacity").value);

    if (!organiserId || !title || !description || !dateTimeInput) {
        showMessage("Please complete all required fields.", "error");
        return;
    }

    if (!Number.isInteger(capacity) || capacity <= 0) {
        showMessage("Capacity must be a whole number greater than zero.", "error");
        return;
    }

    const dateTime = new Date(dateTimeInput);

    if (Number.isNaN(dateTime.getTime()) || dateTime <= new Date()) {
        showMessage("Please choose a future date and time.", "error");
        return;
    }

    const newEvent = {
        title: title,
        description: description,
        dateTime: dateTimeInput,
        capacity: capacity,
        organiserId: organiserId
    };

    const submitButton = createEventForm.querySelector(
        'button[type="submit"]'
    );

    submitButton.disabled = true;
    submitButton.textContent = "Creating...";

    try {
        const createdEvent = await apiRequest(
            "/api/events",
            jsonOptions("POST", newEvent)
        );

        showMessage(
            `Event created successfully! Event ID: ${createdEvent.id}. It is awaiting review.`
        );

        createEventForm.reset();
        organiserIdInput.value = organiserId;

        await loadMyEvents();
    } catch (error) {
        showMessage(error.message, "error");
    } finally {
        submitButton.disabled = false;
        submitButton.textContent = "Create event";
    }
});

// 11. Organiser: view their events

async function loadMyEvents() {
    const organiserId = organiserIdInput.value.trim();

    if (!organiserId) {
        showMessage("Please enter your organiser ID.", "error");
        return;
    }

    myEventsList.replaceChildren(
        createElement("p", "", "Loading your events...")
    );

    try {
        const events = await apiRequest(
            `/api/events/organiser/${encodeURIComponent(organiserId)}`
        );

        myEventsList.replaceChildren();

        if (!Array.isArray(events) || events.length === 0) {
            showEmptyState(
                myEventsList,
                "You have not created any events yet."
            );
            return;
        }

        events.forEach(function (event) {
            myEventsList.appendChild(createEventCard(event));
        });
    } catch (error) {
        showEmptyState(myEventsList, "Could not load your events.");
        showMessage(error.message, "error");
    }
}


// 12. Admin: view all events

async function loadAllEvents() {
    adminEventsList.replaceChildren(
        createElement("p", "", "Loading all events...")
    );

    try {
        const events = await apiRequest("/api/events/admin/all");

        adminEventsList.replaceChildren();

        if (!Array.isArray(events) || events.length === 0) {
            showEmptyState(
                adminEventsList,
                "There are no events to review."
            );
            return;
        }

        events.forEach(function (event) {
            const card = createEventCard(event);
            const status = String(event.status || "").toUpperCase();

            if (status !== "PUBLISHED") {
                const publishButton = createElement(
                    "button",
                    "",
                    "Publish event"
                );

                publishButton.type = "button";

                publishButton.addEventListener("click", async function () {
                    publishButton.disabled = true;
                    publishButton.textContent = "Publishing...";

                    try {
                        await apiRequest(
                            `/api/events/${event.id}/publish`,
                            { method: "PATCH" }
                        );

                        showMessage(
                            `Event "${event.title}" has been published.`
                        );

                        await loadAllEvents();
                    } catch (error) {
                        publishButton.disabled = false;
                        publishButton.textContent = "Publish event";
                        showMessage(error.message, "error");
                    }
                });

                card.appendChild(publishButton);
            }

            adminEventsList.appendChild(card);
        });
    } catch (error) {
        showEmptyState(adminEventsList, "Could not load all events.");
        showMessage(error.message, "error");
    }
}


// 13. Switch between demo roles

function changeRole() {
    clearMessage();

    const role = roleSelect.value;

    visitorSection.hidden = role !== "visitor";
    organiserSection.hidden = role !== "organiser";
    adminSection.hidden = role !== "admin";

    if (role === "visitor") {
        loadPublishedEvents();
    } else if (role === "organiser") {
        loadMyEvents();
    } else if (role === "admin") {
        loadAllEvents();
    }
}

// 14. Connect the buttons

roleSelect.addEventListener("change", changeRole);

document.getElementById("refreshEventsButton")
    .addEventListener("click", loadPublishedEvents);

document.getElementById("loadMyEventsButton")
    .addEventListener("click", loadMyEvents);

document.getElementById("loadAllEventsButton")
    .addEventListener("click", loadAllEvents);

// 15. Start the website

changeRole();
