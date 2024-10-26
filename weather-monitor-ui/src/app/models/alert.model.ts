export interface Alert {
    id: number;
    message: string;
    severity: 'info' | 'warning' | 'error';
    timestamp: Date;
}
