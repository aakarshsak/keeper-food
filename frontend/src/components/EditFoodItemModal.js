import React, { useState, useEffect } from 'react';
import { foodItemsAPI } from '../services/api';
import './EditFoodItemModal.css';

const EditFoodItemModal = ({ isOpen, onClose, foodItem, onSave }) => {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    calorie: '',
    quantity: '',
    consumedDate: ''
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  // Initialize form data when food item changes
  useEffect(() => {
    if (foodItem) {
      setFormData({
        name: foodItem.name || '',
        description: foodItem.description || '',
        calorie: foodItem.calorie || '',
        quantity: foodItem.quantity || '',
        consumedDate: foodItem.consumedDate ? 
          new Date(foodItem.consumedDate).toISOString().slice(0, 16) : ''
      });
    }
  }, [foodItem]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    setError(''); // Clear error when user types
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.name.trim()) {
      setError('Food name is required');
      return;
    }

    try {
      setIsLoading(true);
      setError('');
      
      const updateData = {
        name: formData.name.trim(),
        description: formData.description.trim(),
        calorie: formData.calorie ? parseInt(formData.calorie) : null,
        quantity: formData.quantity.trim(),
        consumedDate: formData.consumedDate || null
      };

      await foodItemsAPI.update(foodItem.id, updateData);
      onSave(); // Callback to refresh the list
      onClose(); // Close the modal
      
    } catch (err) {
      setError('Failed to update food item. Please try again.');
      console.error('Error updating food item:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleClose = () => {
    setError('');
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={handleClose}>
      <div className="modal-content edit-modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <div className="modal-title">
            <span className="modal-icon">✏️</span>
            <h3>Edit Food Item</h3>
          </div>
          <button 
            className="modal-close" 
            onClick={handleClose}
            disabled={isLoading}
            aria-label="Close"
          >
            ×
          </button>
        </div>
        
        <form onSubmit={handleSubmit} className="modal-body">
          {error && (
            <div className="alert alert-error">
              <span className="alert-icon">⚠️</span>
              {error}
            </div>
          )}
          
          <div className="form-section">
            <div className="form-group">
              <label htmlFor="edit-name" className="form-label">
                <span className="label-text">Food Name</span>
                <span className="label-required">*</span>
              </label>
              <input
                type="text"
                id="edit-name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                className="form-input"
                placeholder="Enter food name"
                maxLength="100"
                required
                disabled={isLoading}
              />
            </div>

            <div className="form-group">
              <label htmlFor="edit-description" className="form-label">
                <span className="label-text">Description</span>
                <span className="label-optional">(Optional)</span>
              </label>
              <div className="textarea-wrapper">
                <textarea
                  id="edit-description"
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  className="form-textarea"
                  placeholder="Add any additional notes about this food item... (e.g., cooking method, ingredients, taste notes)"
                  maxLength="500"
                  rows="4"
                  disabled={isLoading}
                />
                <div className="character-count">
                  {formData.description.length}/500
                </div>
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="edit-calorie" className="form-label">
                  <span className="label-text">Calories</span>
                  <span className="label-optional">(Optional)</span>
                </label>
                <div className="input-wrapper">
                  <input
                    type="number"
                    id="edit-calorie"
                    name="calorie"
                    value={formData.calorie}
                    onChange={handleChange}
                    className="form-input"
                    placeholder="250"
                    min="0"
                    max="10000"
                    disabled={isLoading}
                  />
                  <span className="input-suffix">cal</span>
                </div>
              </div>

              <div className="form-group">
                <label htmlFor="edit-quantity" className="form-label">
                  <span className="label-text">Quantity</span>
                  <span className="label-optional">(Optional)</span>
                </label>
                <input
                  type="text"
                  id="edit-quantity"
                  name="quantity"
                  value={formData.quantity}
                  onChange={handleChange}
                  className="form-input"
                  placeholder="e.g., 1 cup, 200g, 1 piece"
                  maxLength="50"
                  disabled={isLoading}
                />
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="edit-consumedDate" className="form-label">
                <span className="label-text">Consumed Date & Time</span>
                <span className="label-optional">(Optional)</span>
              </label>
              <input
                type="datetime-local"
                id="edit-consumedDate"
                name="consumedDate"
                value={formData.consumedDate}
                onChange={handleChange}
                className="form-input datetime-input"
                disabled={isLoading}
              />
            </div>
          </div>
          
          <div className="modal-footer">
            <button
              type="button"
              onClick={handleClose}
              className="btn btn-secondary"
              disabled={isLoading}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={isLoading}
            >
              {isLoading ? (
                <>
                  <span className="loading-spinner"></span>
                  Saving...
                </>
              ) : (
                <>
                  <span className="btn-icon">💾</span>
                  Save Changes
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditFoodItemModal; 