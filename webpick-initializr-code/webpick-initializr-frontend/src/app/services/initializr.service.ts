import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProjectRequest } from '../models/project-request.model';

@Injectable({
  providedIn: 'root'
})
export class InitializrService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8081/api/v1/projects/generate';

  /**
   * Sends the project generation request to the backend and returns the raw ZIP file as a Blob.
   */
  generateProject(request: ProjectRequest): Observable<Blob> {
    return this.http.post(this.apiUrl, request, {
      responseType: 'blob'
    });
  }

  /**
   * Helper that creates a temporary download link in the DOM and clicks it programmatically
   * to trigger the browser's native file saving dialog.
   */
  downloadBlob(blob: Blob, filename: string): void {
    const blobUrl = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = blobUrl;
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(blobUrl);
  }
}
