
export class WeatherData {
    temperature: number = 0;
    feelslike: number = 0;
    maincondition: string = "";
    timestamp: Date | null;
  
    constructor(data: any) {
      this.timestamp = data.timestamp ? new Date(data.timestamp) : null;
    }
  
    getFormattedTimestamp(): string {
      if (this.timestamp) {
        return this.timestamp.toLocaleString('en-US', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit',
        });
      }
      return 'Unknown time';
    }
  }
  