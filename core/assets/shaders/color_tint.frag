#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform vec4 u_tintColor;

void main() {
  vec4 texColor = texture2D(u_texture, v_texCoords);
  gl_FragColor = texColor.a * u_tintColor + texColor;
}