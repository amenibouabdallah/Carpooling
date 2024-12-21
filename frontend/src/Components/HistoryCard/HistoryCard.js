import React, { useState } from 'react';
import './HistoryCard.css';

const HistoryCard = ({ id, driverId, departurePoint, dateTime, possiblePlaces, pricePerPlace, comment, arrivalPoint }) => {
    const [showModal, setShowModal] = useState(false);

    const handleCancelClick = () => {
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
    };

    const handleConfirmCancel = () => {
        // Add logic to handle reservation cancellation
        setShowModal(false);
    };

    return (
        <div className="history-card">
            <h2>History Item</h2>
            <p>Driver ID: {driverId}</p>
            <p>Departure Point: {departurePoint}</p>
            <p>Date and Time: {dateTime}</p>
            <p>Possible Places: {possiblePlaces}</p>
            <p>Price per Place: {pricePerPlace}</p>
            <p>Comment: {comment}</p>
            <p>Arrival Point: {arrivalPoint}</p>
            <button onClick={handleCancelClick}>Cancel</button>

            {showModal && (
                <div className="modal">
                    <div className="modal-content">
                        <p>Would you like to remove this reservation?</p>
                        <button onClick={handleConfirmCancel}>Yes</button>
                        <button onClick={handleCloseModal}>No</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default HistoryCard;