/** Format a Date or ISO string as `yyyy-MM-dd`. */
export function toDateString(value: Date | string | undefined | null): string {
  if (!value) return '';
  const d = typeof value === 'string' ? new Date(value) : value;
  if (isNaN(d.getTime())) return '';
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${y}-${m}-${day}`;
}

/** Take only the date portion of a backend datetime string ("yyyy-MM-dd HH:mm:ss" or ISO). */
export function dateOnly(value: string | undefined | null): string {
  if (!value) return '';
  const trimmed = value.trim();
  if (trimmed.length >= 10) return trimmed.slice(0, 10);
  return trimmed;
}
