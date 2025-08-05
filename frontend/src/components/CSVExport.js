import React, { useState } from 'react';
import { foodItemsAPI } from '../services/api';

const CSVExport = () => {
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [isExporting, setIsExporting] = useState(false);
  const [error, setError] = useState('');

  const handleExport = async () => {
    try {
      setIsExporting(true);
      setError('');

      // Validate date range
      if (startDate && endDate && new Date(startDate) > new Date(endDate)) {
        setError('Start date cannot be after end date');
        return;
      }

      const response = await foodItemsAPI.exportToCSV(startDate || null, endDate || null);
      
      // Create a download link
      const blob = new Blob([response.data], { type: 'text/csv' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      
      // Generate filename
      let filename = 'food_items';
      if (startDate && endDate) {
        filename += `_${startDate}_to_${endDate}`;
      } else if (startDate) {
        filename += `_from_${startDate}`;
      } else {
        filename += '_all_time';
      }
      filename += '.csv';
      
      link.setAttribute('download', filename);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
      
    } catch (err) {
      setError('Failed to export CSV. Please try again.');
      console.error('Error exporting CSV:', err);
    } finally {
      setIsExporting(false);
    }
  };

  const handleClearDates = () => {
    setStartDate('');
    setEndDate('');
    setError('');
  };

  return (
    <div className="csv-export-container">
      <h3>📊 Export Food History</h3>
      <p>Download your food tracking data as a CSV file</p>
      
      {error && (
        <div className="alert alert-error" style={{ marginBottom: '1rem' }}>
          {error}
        </div>
      )}
      
      <div className="export-controls">
        <div className="date-range">
          <div className="form-group">
            <label htmlFor="startDate">Start Date (optional):</label>
            <input
              type="date"
              id="startDate"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
              className="form-control"
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="endDate">End Date (optional):</label>
            <input
              type="date"
              id="endDate"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
              className="form-control"
            />
          </div>
        </div>
        
        <div className="export-actions">
          <button
            onClick={handleExport}
            disabled={isExporting}
            className="btn btn-primary"
          >
            {isExporting ? '📥 Exporting...' : '📥 Download CSV'}
          </button>
          
          <button
            onClick={handleClearDates}
            className="btn btn-secondary"
            disabled={isExporting}
          >
            Clear Dates
          </button>
        </div>
        
        <div className="export-info">
          <small>
            {!startDate && !endDate ? (
              'Export all food items (all time)'
            ) : startDate && endDate ? (
              `Export items from ${startDate} to ${endDate}`
            ) : startDate ? (
              `Export items from ${startDate} onwards`
            ) : (
              `Export items up to ${endDate}`
            )}
          </small>
        </div>
      </div>
    </div>
  );
};

export default CSVExport; 