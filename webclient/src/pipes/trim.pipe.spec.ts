import { TrimPipe } from './trim.pipe';

const STR_LEN_10 = '0123456789';
const STR_LEN_20 = STR_LEN_10 + STR_LEN_10;
const STR_LEN_30 = STR_LEN_20 + STR_LEN_10;
const STR_LEN_40 = STR_LEN_20 + STR_LEN_20;

describe('TrimPipe', () => {
  it('create an instance', () => {
    const pipe = new TrimPipe();
    expect(pipe).toBeTruthy();
  });

  it('should not trim when not needed', () => {
    const pipe = new TrimPipe();
    expect(pipe.transform(STR_LEN_10)).toBe(STR_LEN_10);
    expect(pipe.transform(STR_LEN_30)).toBe(STR_LEN_30);
    expect(pipe.transform(STR_LEN_30)).toBe(STR_LEN_30);
    expect(pipe.transform(STR_LEN_40, 40)).toBe(STR_LEN_40);
  });

  it('should trim to proper length', () => {
    const pipe = new TrimPipe();
    expect(pipe.transform(STR_LEN_10, 6)).toBe('012...');
    expect(pipe.transform(STR_LEN_20, 4)).toBe('0...');
  });

  it('should not trim when length too low', () => {
    const pipe = new TrimPipe();
    expect(pipe.transform('abc', 2)).toBe('abc');
  });
});
