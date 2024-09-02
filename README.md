# Doctor's Appointment Scheduling Application

## Overview

The Doctor's Appointment Scheduling Application is a simple yet powerful tool designed to help a single doctor manage their appointment schedule efficiently. The application allows the doctor to define available time slots, view scheduled and available appointments, and manage bookings. Patients can easily view available slots, book appointments, and review their scheduled appointments using this application.

## Features

### Doctor's Features

- **Add Available Time Slots:**
  - Doctors can set start and end times for their availability each day.
  - The system automatically breaks down these times into 30-minute intervals.
  - Time periods shorter than 30 minutes are ignored.

- **View Appointments:**
  - Doctors can view all 30-minute slots, categorized as open (available) or taken (booked).
  - For taken slots, the patient’s name and phone number are displayed.

- **Delete Open Appointments:**
  - Doctors can delete open appointments.
  - Appointments that have been booked by a patient cannot be deleted.
  - The system handles concurrency issues, ensuring that simultaneous booking and deletion actions are managed properly.

### Patient's Features

- **View Available Appointments:**
  - Patients can browse all open appointments for a specific day.

- **Book an Appointment:**
  - Patients can book an open appointment by providing their name and phone number.
  - The system validates inputs and ensures the slot is still available.

- **View Personal Appointments:**
  - Patients can view their booked appointments by providing their phone number.
  - If multiple appointments are booked under the same number, all of them will be displayed.
