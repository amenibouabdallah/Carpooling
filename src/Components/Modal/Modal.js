import React from 'react';
import './Modal.css';

const Modal = ({ drive, onClose, onConfirm }) => {
    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h2 className='confirm-ride'>Confirm Ride</h2>
                <div className="modal-buttons">
                    <button onClick={onConfirm}>Confirm Ride</button>
                    <button onClick={onClose}>Cancel</button>
                </div>
            </div>
        </div>
    );
};

export default Modal;