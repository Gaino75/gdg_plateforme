export const INK = "#14181F";
export const MUTED = "#6B7280";
export const PAPER = "#F5F6F3";
export const SURFACE = "#FFFFFF";
export const LINE = "#E4E7E2";
export const PETROL = "#0B5563";
export const PETROL_DARK = "#082F38";
export const FLAME = "#F2994A";
export const SUCCESS = "#2E9E6D";
export const DANGER = "#D6455B";

export const FONT_DISPLAY = "'Space Grotesk', sans-serif";
export const FONT_BODY = "'Inter', sans-serif";
export const FONT_MONO = "'IBM Plex Mono', monospace";

export function FontLoader() {
  return (
    <style>{`
      @import url('https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@500;600;700&family=Inter:wght@400;500;600&family=IBM+Plex+Mono:wght@500;600&display=swap');
    `}</style>
  );
}

export function Gauge({ value = 68, size = 180, accent = FLAME, track = "rgba(255,255,255,0.15)" }) {
  const angle = -90 + (value / 100) * 180;
  const rad = (angle * Math.PI) / 180;
  const cx = size / 2;
  const cy = size / 2 + 6;
  const r = size / 2 - 18;
  const needleX = cx + r * 0.82 * Math.cos(rad);
  const needleY = cy + r * 0.82 * Math.sin(rad);
  const ticks = Array.from({ length: 9 }, (_, i) => {
    const a = -90 + (i / 8) * 180;
    const rr = (a * Math.PI) / 180;
    const x1 = cx + (r - 2) * Math.cos(rr);
    const y1 = cy + (r - 2) * Math.sin(rr);
    const x2 = cx + (r - 12) * Math.cos(rr);
    const y2 = cy + (r - 12) * Math.sin(rr);
    return { x1, y1, x2, y2 };
  });
  return (
    <svg width={size} height={size / 2 + 30} viewBox={`0 0 ${size} ${size / 2 + 30}`}>
      <path d={`M ${cx - r} ${cy} A ${r} ${r} 0 0 1 ${cx + r} ${cy}`} fill="none" stroke={track} strokeWidth="10" strokeLinecap="round" />
      <path
        d={`M ${cx - r} ${cy} A ${r} ${r} 0 0 1 ${cx + r} ${cy}`}
        fill="none"
        stroke={accent}
        strokeWidth="10"
        strokeLinecap="round"
        strokeDasharray={`${(value / 100) * Math.PI * r} ${Math.PI * r}`}
      />
      {ticks.map((t, i) => (
        <line key={i} x1={t.x1} y1={t.y1} x2={t.x2} y2={t.y2} stroke={track} strokeWidth="2" />
      ))}
      <circle cx={cx} cy={cy} r="5" fill={SURFACE} />
      <line x1={cx} y1={cy} x2={needleX} y2={needleY} stroke={SURFACE} strokeWidth="3" strokeLinecap="round" />
    </svg>
  );
}

export function Brand({ dark }) {
  return (
    <div className="flex items-center gap-2">
      <div className="w-9 h-9 rounded-lg flex items-center justify-center" style={{ background: dark ? "rgba(255,255,255,0.12)" : PETROL }}>
        <span style={{ color: "#fff", fontSize: 16 }}>⛽</span>
      </div>
      <span className="text-lg tracking-tight" style={{ fontFamily: FONT_DISPLAY, fontWeight: 600, color: dark ? SURFACE : INK }}>
        GPG
      </span>
    </div>
  );
}

export function Field({ icon: Icon, label, type = "text", placeholder, rightSlot, defaultValue }) {
  return (
    <label className="block mb-4">
      <span className="block text-xs mb-1.5" style={{ color: MUTED, fontFamily: FONT_BODY, fontWeight: 500 }}>{label}</span>
      <div className="flex items-center gap-2 px-3.5 py-2.5 rounded-xl" style={{ border: `1px solid ${LINE}`, background: SURFACE }}>
        {Icon && <Icon size={16} color={MUTED} />}
        <input
          type={type}
          placeholder={placeholder}
          defaultValue={defaultValue}
          className="flex-1 outline-none text-sm bg-transparent"
          style={{ color: INK, fontFamily: FONT_BODY }}
        />
        {rightSlot}
      </div>
    </label>
  );
}

export function PrimaryButton({ children, full, onClick }) {
  return (
    <button
      onClick={onClick}
      className={`flex items-center justify-center gap-2 rounded-xl py-3 text-sm transition-transform active:scale-[0.98] ${full ? "w-full" : ""}`}
      style={{ background: PETROL, color: "#fff", fontFamily: FONT_BODY, fontWeight: 600 }}
    >
      {children}
    </button>
  );
}

export function AuthShell({ children, panelTitle, panelSub, stat, statLabel, roleLabel }) {
  return (
    <div className="min-h-screen w-full flex" style={{ background: PAPER, fontFamily: FONT_BODY }}>
      <FontLoader />
      <div className="hidden md:flex flex-col justify-between w-[42%] p-10" style={{ background: PETROL_DARK }}>
        <Brand dark />
        <div>
          {roleLabel && (
            <p className="text-4xl md:text-5xl mb-6 leading-none" style={{ fontFamily: FONT_DISPLAY, fontWeight: 700, color: FLAME, letterSpacing: "-0.01em" }}>
              {roleLabel}
            </p>
          )}
          <p className="text-xs uppercase tracking-widest mb-3" style={{ color: "rgba(255,255,255,0.5)", letterSpacing: "0.12em" }}>{panelSub}</p>
          <h2 className="text-2xl mb-6 leading-snug" style={{ fontFamily: FONT_DISPLAY, fontWeight: 600, color: SURFACE }}>{panelTitle}</h2>
          <div className="flex items-end gap-6">
            <Gauge value={72} size={160} />
            <div>
              <p className="text-3xl" style={{ fontFamily: FONT_MONO, fontWeight: 600, color: SURFACE }}>{stat}</p>
              <p className="text-xs mt-1" style={{ color: "rgba(255,255,255,0.55)" }}>{statLabel}</p>
            </div>
          </div>
        </div>
        <p className="text-xs" style={{ color: "rgba(255,255,255,0.4)" }}>Réseau Total · Ola Energy · Tradex · Mrs · Green Oil</p>
      </div>
      <div className="flex-1 flex items-center justify-center p-6 md:p-10">
        <div className="w-full max-w-md">{children}</div>
      </div>
    </div>
  );
}

export function AppTopbar({ active, items = [] }) {
  return (
    <div className="flex items-center justify-between px-8 py-4" style={{ borderBottom: `1px solid ${LINE}`, background: SURFACE }}>
      <Brand />
      <div className="hidden md:flex items-center gap-6">
        {items.map((it) => (
          <span
            key={it.id}
            className="text-sm cursor-pointer pb-1"
            style={{
              color: active === it.id ? INK : MUTED,
              fontWeight: active === it.id ? 600 : 500,
              borderBottom: active === it.id ? `2px solid ${PETROL}` : "2px solid transparent",
            }}
          >
            {it.label}
          </span>
        ))}
      </div>
      <div className="w-9 h-9 rounded-full flex items-center justify-center text-xs" style={{ background: PETROL, color: "#fff", fontWeight: 600 }}>EK</div>
    </div>
  );
}